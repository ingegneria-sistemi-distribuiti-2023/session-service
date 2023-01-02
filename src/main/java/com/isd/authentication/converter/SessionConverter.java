package com.isd.authentication.converter;

import com.isd.authentication.domain.Session;
import com.isd.authentication.dto.SessionDTO;

public class SessionConverter {

    public static SessionDTO toDTO(Session session) {
        SessionDTO dto = new SessionDTO();
        dto.setSessionId(session.getSessionId());
        dto.setUserId(session.getUserId());
        dto.setData(session.getData());
        dto.setStartTime(session.getStartTime());
        dto.setEndTime(session.getEndTime());
        return dto;
    }

    public static Session toEntity(SessionDTO dto) {
        Session session = new Session();
        session.setSessionId(dto.getSessionId());
        session.setUserId(dto.getUserId());
        session.setData(dto.getData());
        session.setStartTime(dto.getStartTime());
        session.setEndTime(dto.getEndTime());
        return session;
    }
}