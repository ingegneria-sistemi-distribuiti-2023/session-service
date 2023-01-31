package com.isd.session;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.util.*;

import com.isd.session.commons.CurrencyEnum;
import com.isd.session.converter.SessionConverter;
import com.isd.session.domain.Session;
import com.isd.session.dto.BetDTO;
import com.isd.session.dto.SessionDTO;
import com.isd.session.dto.UserDataDTO;
import com.isd.session.repository.SessionRepository;
import com.isd.session.service.SessionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private SessionService sessionService;

    private List<Session> sessions;

    /*
     * Util function that generate fake data used to make tests
     */
    public static UserDataDTO generateDummyData() {
        UserDataDTO userData = new UserDataDTO();
        userData.setUserId(12345);
        userData.setSessionId("abcdefg");
        userData.setListOfBets(new ArrayList<>());

        for (int i = 0; i < 2; i++) {
            BetDTO bet = new BetDTO();
            bet.setTs((long) i);
            bet.setBetValue(1000);
            bet.setCurrency(CurrencyEnum.USD);
            userData.getListOfBets().add(bet);
        }

        return userData;
    }


    @Before
    public void setUp() {

        UserDataDTO data1 = generateDummyData();

        // initialize the sessions list
        sessions = new ArrayList<>();
        Session session1 = new Session();
        session1.setSessionId(1);
        session1.setUserId(data1.getUserId());
        session1.setSessionKey(UUID.randomUUID().toString());
        session1.setStartTime(new Timestamp(new Date().getTime()));
        session1.setEndTime(new Timestamp(new Date().getTime() + (24 * 60 * 60 * 1000)));
        sessions.add(session1);

        UserDataDTO data2 = generateDummyData();

        Session session2 = new Session();
        session2.setSessionId(2);
        session2.setUserId(data2.getUserId());
        session2.setSessionKey(UUID.randomUUID().toString());
        session2.setStartTime(new Timestamp(new Date().getTime()));
        session2.setEndTime(new Timestamp(new Date().getTime() + (24 * 60 * 60 * 1000)));
        sessions.add(session2);
    }

    /*
     * getAllSessions() must return all session avaiable
     */
    @Test
    public void getAllSessionShouldReturnAllSessions() {
        when(sessionRepository.findAll()).thenReturn(sessions);
        List<SessionDTO> sessionDTOs = sessionService.getAllSessions();
        assertNotNull(sessionDTOs);
        assertEquals(2, sessionDTOs.size());
    }

    /*
     * save() must save all information
     */
    @Test
    public void createSessionShouldReturnEqualSession() {
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

    /*
     * save() must update existing session
     */
    @Test
    public void updateSessionShouldReturnUpdatedSession() {
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

    /*
     * deleteSession() must delete on db
     */
    @Test
    public void deleteSessionShouldExecutedInRepository() {
        sessionService.deleteSession(1);
        //verify that the deleteBySessionId method of sessionRepository is called
        verify(sessionRepository, times(1)).deleteBySessionId(1);
    }

}