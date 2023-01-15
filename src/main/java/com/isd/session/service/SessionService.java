package com.isd.session.service;

import com.isd.session.converter.SessionConverter;
import com.isd.session.domain.Session;
import com.isd.session.dto.SessionDTO;
import com.isd.session.dto.UserDataDTO;
import com.isd.session.repository.SessionRepository;
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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Transactional
@Service
public class SessionService {
    RedisTemplate<String, UserDataDTO> redisTemplate;
    private SessionRepository sessionRepository;

    @Value("${session.expire.delta}")
    private static int EXPIRE_DELTA;

    public SessionService(RedisTemplate<String, UserDataDTO> redisTemplate, SessionRepository sessionRepository) {
        this.redisTemplate = redisTemplate;
        this.sessionRepository = sessionRepository;
    }

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
        Session session = sessionRepository.findByUserId(userId);
        return session != null? SessionConverter.toDTO(session) : null;
    }

    public UserDataDTO createAndUpdateSession(UserDataDTO dto){
        // check if a session ID is present in the database using the sessionService
        SessionDTO userSession = this.getByUserId(dto.getUserId());
        String sessionKey = null;

        // the expire timestamp is calculated
        Long expireTimestamp = new Date().getTime() + TimeUnit.HOURS.toMillis(EXPIRE_DELTA);
        // if the session does not exist, a new session is created
        // the session is creates also if it's present and the expire timestamp is expired
        // the old session will be deleted
        if(userSession == null || userSession.getEndTime().getTime() < new Date().getTime()) {
            // if the session exists, it is deleted
            if(userSession != null) {
                sessionRepository.deleteBySessionId(userSession.getSessionId());
//                this.deleteSession(userSession.getSessionId());
                // the data stored in the redis database is deleted
                redisTemplate.delete(userSession.getSessionKey());
            }
            SessionDTO sessionDTO = new SessionDTO();
            sessionDTO.setUserId(dto.getUserId());
            sessionDTO.setStartTime(new Timestamp(new Date().getTime()));
            sessionDTO.setEndTime(new Timestamp(expireTimestamp));
            // generate a new session id if a new session is being created
            sessionKey = UUID.randomUUID().toString();
            dto.setSessionId(sessionKey);
            sessionDTO.setSessionKey(sessionKey);
            this.createSession(sessionDTO);
        } else {
            sessionKey = userSession.getSessionKey();
            // if the session exists, the expire timestamp is updated
            userSession.setEndTime(new Timestamp(expireTimestamp));
            this.updateSession(userSession);
        }

        // save the session to the redis database
        redisTemplate.opsForValue().set(sessionKey, dto);

        return dto;
    }

    public UserDataDTO getSessionByUserId(Integer userId){
        // check if a session ID is present in the database using the sessionService
        SessionDTO userSession = this.getByUserId(userId);
        UserDataDTO session = null;

        // the expire timestamp is calculated
        Long expireTimestamp = new Date().getTime() + TimeUnit.HOURS.toMillis(EXPIRE_DELTA);
        // if it's expired the session is deleted
        if (userSession != null && userSession.getEndTime().getTime() < new Date().getTime()) {
            sessionRepository.deleteBySessionId(userSession.getSessionId());
            // the data stored in the redis database is deleted
            redisTemplate.delete(userSession.getSessionKey());
            userSession = null;
        }
        if (userSession == null) {
            // if sessiond does not exist create a new one and return

            SessionDTO sessionDTO = new SessionDTO();
            sessionDTO.setUserId(userId);
            sessionDTO.setStartTime(new Timestamp(new Date().getTime()));
            sessionDTO.setEndTime(new Timestamp(expireTimestamp));
            // generate a new session id if a new session is being created
            String sessionKey = UUID.randomUUID().toString();
            sessionDTO.setSessionKey(sessionKey);
            this.createSession(sessionDTO);

            // generate userData to save in Redis
            UserDataDTO newUserData = new UserDataDTO();
            newUserData.setSessionId(sessionKey);
            newUserData.setListOfBets(new LinkedList<>());
            newUserData.setUserId(userId);

            redisTemplate.opsForValue().set(sessionKey, newUserData);
            newUserData.setSessionId(sessionKey);
            session = newUserData;
        } else {
            // if the session exists, the expire timestamp is updated
            userSession.setEndTime(new Timestamp(expireTimestamp));
            this.updateSession(userSession);
            // get the session from the redis database
            session = redisTemplate.opsForValue().get(userSession.getSessionKey());
        }

        return session;
    }

}