package com.isd.session.service;

import com.isd.session.converter.SessionConverter;
import com.isd.session.domain.Session;
import com.isd.session.dto.SessionDTO;
import com.isd.session.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

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

    public void deleteSession(String sessionId) {
        sessionRepository.deleteById(sessionId);
    }

    public SessionDTO getByUserId(Integer userId) {
        Session session = sessionRepository.findByUserId(userId);
        return session != null? SessionConverter.toDTO(session) : null;
    }
}