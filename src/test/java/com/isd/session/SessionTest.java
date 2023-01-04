package com.isd.session;

import com.isd.session.converter.SessionConverter;
import com.isd.session.domain.Session;
import com.isd.session.dto.SessionDTO;
import com.isd.session.repository.SessionRepository;
import com.isd.session.service.SessionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SessionTest {
    @Mock
    SessionRepository sessionRepository;

    @InjectMocks
    SessionService sessionService;


    @Before
    public void setup() {
        // initialize i mock object
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllSessions() {
        // Configura il comportamento del mock object sessionRepository
        List<Session> sessions = new ArrayList<>();

        Session s1 = new Session();
        s1.setSessionId("abc");
        s1.setUserId(1);
        byte[] s1bytes = "Any String you want".getBytes();
        s1.setData(s1bytes);

        Date session_start = new Date();
        Date session_expiry = new Date(session_start.getTime() + 60 * 60 * 1000);

        s1.setStartTime(new Timestamp(session_start.getTime()));
        s1.setEndTime(new Timestamp(session_expiry.getTime()));
        sessions.add(s1);

        Session s2 = new Session();
        s2.setSessionId("efg");
        s2.setUserId(1);
        byte[] s2bytes = "Any String you want edited".getBytes();
        s2.setData(s2bytes);
        s2.setStartTime(new Timestamp(session_start.getTime()));
        s2.setEndTime(new Timestamp(session_expiry.getTime()));

        sessions.add(s2);

        when(sessionRepository.findAll()).thenReturn(sessions);

        // Chiama il metodo getAllSessions
        List<SessionDTO> result = sessionService.getAllSessions();

        List<Session> result1 = new ArrayList<>();

        for (SessionDTO sess: result){
            result1.add(SessionConverter.toEntity(sess));
        }

        // Verifica che il mock object sia stato utilizzato correttamente
        assertEquals(2, sessions.size());
        assertTrue(result1.contains(sessions.get(0)));
        assertTrue(result1.contains(sessions.get(1)));

    }

//    TODO:
//    @Test
//    public void testGetSessionById() {
//        // Configura il comportamento del mock object sessionRepository
//        Session session = new Session("1", "Session 1", LocalDateTime.now(), LocalDateTime.now().plusHours(1));
//        when(sessionRepository.findById("1")).thenReturn(Optional.of(session));
//
//        // Chiama il metodo getSessionById
//        SessionDTO result = sessionService.getSessionById("1");
//
//        // Verifica che il mock object sia stato utilizzato correttamente
//        verify(sessionRepository).findById("1");
//
//        // Verifica che il risultato sia quello atteso
//        assertEquals("1", result.getId());
//        assertEquals("Session 1", result.getName());
//
//        // Chiama il metodo getSessionById con un ID inesistente
//        result = sessionService.getSessionById("999");
//
//        // Verifica che il mock object sia stato utilizzato correttamente
//        verify(sessionRepository).findById("999");
//
//        // Verifica che il risultato sia null
//        assertNull(result);
//    }
}
