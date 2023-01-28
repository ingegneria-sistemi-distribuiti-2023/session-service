package com.isd.session.service;

import com.isd.session.converter.SessionConverter;
import com.isd.session.domain.Session;
import com.isd.session.dto.SessionDTO;
import com.isd.session.dto.UserDataDTO;
import com.isd.session.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class SessionService {
    private final RedisTemplate<String, UserDataDTO> redisTemplate;
    private final SessionRepository sessionRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionService.class);

    @Value("${session.expire.delta}")
    private static int EXPIRE_DELTA;

    public List<SessionDTO> getAllSessions() {
        List<Session> sessions = sessionRepository.findAll();
        return sessions.stream().map(SessionConverter::toDTO).collect(Collectors.toList());
    }

    public SessionDTO getSessionById(String sessionId) {
        Session session = sessionRepository.findById(sessionId).orElse(null);
        return session != null ? SessionConverter.toDTO(session) : null;
    }

    public SessionDTO createSession(SessionDTO sessionDTO) {
        Session session = SessionConverter.toEntity(sessionDTO);
        session = sessionRepository.save(session);
        return SessionConverter.toDTO(session);
    }

    public SessionDTO updateSession(SessionDTO sessionDTO) {
        Session session = SessionConverter.toEntity(sessionDTO);
        session = sessionRepository.save(session);
        return SessionConverter.toDTO(session);
    }

    public void deleteSession(Integer sessionId) {
        sessionRepository.deleteBySessionId(sessionId);
    }

    public SessionDTO getByUserId(Integer userId) {
        Session session = sessionRepository.findOneByUserId(userId);
        return session != null? SessionConverter.toDTO(session) : null;
    }

    public SessionDTO createSessionDtoByUserId(Integer userId, boolean isNew){
        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setUserId(userId);
        if (isNew){
            sessionDTO.setStartTime(new Timestamp(new Date().getTime()));
        }
        sessionDTO.setEndTime(new Timestamp(new Date().getTime() + (24 * 60 * 60 * 1000)));
        // generate a new session id if a new session is being created
        String sessionKey = UUID.randomUUID().toString();
        sessionDTO.setSessionKey(sessionKey);
        this.createSession(sessionDTO);
        return sessionDTO;
    }

    public SessionDTO removeSessionIfExpired(SessionDTO userSession){
        if (userSession != null ){

            Long tsEnd = userSession.getEndTime().getTime();

            long diff = tsEnd - new Date().getTime();
            if (diff < 0 ) {
                // it means that is expired
                LOGGER.info("Expired: true");

                sessionRepository.deleteBySessionId(userSession.getSessionId());
                // the data stored in the redis database is deleted
                redisTemplate.delete(userSession.getSessionKey());
                return null;
            }
        }
        return userSession;
    }

    public UserDataDTO createAndUpdateSession(UserDataDTO dto){
        // check if a session ID is present in the database using the sessionService
        SessionDTO userSession = this.getByUserId(dto.getUserId());
        String sessionKey = null;

        userSession = this.removeSessionIfExpired(userSession);

        if(userSession == null ) {
            userSession = this.createSessionDtoByUserId(dto.getUserId(), true);
        } else {
            // if the session exists, the expire timestamp is updated after one day
            userSession.setEndTime(new Timestamp(new Date().getTime() + (24 * 60 * 60 * 1000)));
            this.updateSession(userSession);
        }

        sessionKey = userSession.getSessionKey();

        // save the session to the redis database
        redisTemplate.opsForValue().set(sessionKey, dto);

        return dto;
    }

    public UserDataDTO getSessionByUserId(Integer userId){
        // check if a session ID is present in the database using the sessionService
        SessionDTO userSession = this.getByUserId(userId);
        UserDataDTO session = null;

        userSession = this.removeSessionIfExpired(userSession);

        if (userSession == null) {
            // if sessiond does not exist create a new one and return
            userSession = this.createSessionDtoByUserId(userId, true);
            // generate userData to save in Redis
            session = new UserDataDTO();
            session.setSessionId(userSession.getSessionKey());
            // initialize an empty array of list of bets, in this way is going to be ready to add bet
            session.setListOfBets(new LinkedList<>());
            session.setUserId(userId);
            redisTemplate.opsForValue().set(userSession.getSessionKey(), session);
        } else {
            // if the session exists, the expire timestamp is updated
            userSession.setEndTime(new Timestamp(new Date().getTime() + (24 * 60 * 60 * 1000)));
            this.updateSession(userSession);
            // get the session from the redis database
            session = redisTemplate.opsForValue().get(userSession.getSessionKey());
        }

        return session;
    }

}