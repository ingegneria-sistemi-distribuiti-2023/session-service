package com.isd.authentication.service;

import com.isd.authentication.converter.SessionConverter;
import com.isd.authentication.domain.Session;
import com.isd.authentication.dto.SessionDTO;
import com.isd.authentication.repository.SessionRepository;
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
}