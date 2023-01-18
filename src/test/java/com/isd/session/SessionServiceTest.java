package com.isd.session;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.util.*;

import com.isd.session.converter.SessionConverter;
import com.isd.session.domain.Session;
import com.isd.session.dto.SessionDTO;
import com.isd.session.dto.UserDataDTO;
import com.isd.session.repository.SessionRepository;
import com.isd.session.service.SessionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private RedisTemplate<String, UserDataDTO> redisTemplate;

    @InjectMocks
    private SessionService sessionService;

    private List<Session> sessions;

    @Before
    public void setUp() {
        // initialize the sessions list
        sessions = new ArrayList<>();
        Session session1 = new Session();
        session1.setSessionId(1);
        session1.setUserId(1);
        session1.setSessionKey(UUID.randomUUID().toString());
        session1.setStartTime(new Timestamp(new Date().getTime()));
        session1.setEndTime(new Timestamp(new Date().getTime() + (24 * 60 * 60 * 1000)));
        sessions.add(session1);

        Session session2 = new Session();
        session2.setSessionId(2);
        session2.setUserId(2);
        session2.setSessionKey(UUID.randomUUID().toString());
        session2.setStartTime(new Timestamp(new Date().getTime()));
        session2.setEndTime(new Timestamp(new Date().getTime() + (24 * 60 * 60 * 1000)));
        sessions.add(session2);
    }

    @Test
    public void testGetAllSessions() {
        when(sessionRepository.findAll()).thenReturn(sessions);
        List<SessionDTO> sessionDTOs = sessionService.getAllSessions();
        assertNotNull(sessionDTOs);
        assertEquals(2, sessionDTOs.size());
    }

    @Test
    public void testCreateSession() {
        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setSessionId(3);
        sessionDTO.setUserId(3);
        sessionDTO.setSessionKey(UUID.randomUUID().toString());
        sessionDTO.setStartTime(new Timestamp(new Date().getTime()));
        sessionDTO.setEndTime(new Timestamp(new Date().getTime() + (24 * 60 * 60 * 1000)));
        when(sessionRepository.save(any(Session.class))).thenReturn(SessionConverter.toEntity(sessionDTO));
        SessionDTO result = sessionService.createSession(sessionDTO);
        assertNotNull(result);
        assertEquals(3, result.getSessionId().intValue());
    }

    @Test
    public void testUpdateSession() {
        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setSessionId(1);
        sessionDTO.setUserId(1);
        sessionDTO.setSessionKey(UUID.randomUUID().toString());
        sessionDTO.setStartTime(new Timestamp(new Date().getTime()));
        sessionDTO.setEndTime(new Timestamp(new Date().getTime() + (24 * 60 * 60 * 1000)));
        when(sessionRepository.save(any(Session.class))).thenReturn(SessionConverter.toEntity(sessionDTO));
        SessionDTO result = sessionService.updateSession(sessionDTO);
        assertNotNull(result);
        assertEquals(1, result.getSessionId().intValue());
    }

    @Test
    public void testDeleteSession() {
        sessionService.deleteSession(1);
        //verify that the deleteBySessionId method of sessionRepository is called
        verify(sessionRepository, times(1)).deleteBySessionId(1);
    }

    @Test
    public void testGetByUserIdNotFound() {
        when(sessionRepository.findOneByUserId(3)).thenReturn(null);
        SessionDTO sessionDTO = sessionService.getByUserId(3);
        assertNull(sessionDTO);
    }

//    @Test
//    public void testRemoveSessionIfExpired() {
//        SessionDTO sessionDTO = new SessionDTO();
//        sessionDTO.setSessionId(1);
//        sessionDTO.setUserId(1);
//        sessionDTO.setSessionKey(UUID.randomUUID().toString());
//        sessionDTO.setStartTime(new Timestamp(new Date().getTime()));
//        sessionDTO.setEndTime(new Timestamp(new Date().getTime() - (24 * 60 * 60 * 1000)));
//        when(sessionRepository.deleteBySessionId(1)).thenReturn(null);
//        SessionDTO result = sessionService.removeSessionIfExpired(sessionDTO);
//        assertNull(result);
//        //verify that the deleteBySessionId method of sessionRepository is called
//        verify(sessionRepository, times(1)).deleteBySessionId(1);
//        verify(redisTemplate, times(1)).delete(sessionDTO.getSessionKey());
//    }

//    @Test
//    public void testCreateAndUpdateSession() {
//        UserDataDTO dto = new UserDataDTO();
//        dto.setUserId(1);
//        dto.setName("test");
//        dto.setAge(25);
//        when(sessionRepository.findOneByUserId(1)).thenReturn(sessions.get(0));
//        when(sessionRepository.save(any(Session.class))).thenReturn(sessions.get(0));
//        UserDataDTO result = sessionService.createAndUpdateSession(dto);
//        assertNotNull(result);
//        assertEquals(1, result.getUserId().intValue());
//        verify(redisTemplate, times(1)).opsForValue().set(sessions.get(0).getSessionKey(), dto);
//    }

//    @Test
//    public void testGetSessionByUserId() {
//        UserDataDTO dto = new UserDataDTO();
//        dto.setUserId(1);
//        dto.setName("test");
//        dto.setAge(25);
//        when(sessionRepository.findOneByUserId(1)).thenReturn(sessions.get(0));
//        when(redisTemplate.opsForValue().get(sessions.get(0).getSessionKey())).thenReturn(dto);
//        UserDataDTO result = sessionService.getSessionByUserId(1);
//        assertNotNull(result);
//        assertEquals(1, result.getUserId().intValue());
//    }

//    @Test
//    public void testGetSessionByUserIdNotFound() {
//        when(sessionRepository.findOneByUserId(3)).thenReturn(null);
//        UserDataDTO result = sessionService.getSessionByUserId(3);
//        assertNull(result);
//    }


}