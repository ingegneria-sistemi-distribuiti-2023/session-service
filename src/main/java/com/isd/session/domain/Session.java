package com.isd.session.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "session")
public class Session {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sessionId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "session_id")
    private String sessionKey;

    @Column(name = "start_time")
    private Timestamp startTime;
    @Column(name = "end_time")
    private Timestamp endTime;

    public Session() {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Session session = (Session) obj;

        if (sessionId != null ? !sessionId.equals(session.sessionId) : session.sessionId != null) return false;
        if (userId != null ? !userId.equals(session.userId) : session.userId != null) return false;
        if (sessionKey != null ? !sessionKey.equals(session.sessionKey) : session.sessionKey != null) return false;
        if (startTime != null ? !startTime.equals(session.startTime) : session.startTime != null) return false;
        return endTime != null ? endTime.equals(session.endTime) : session.endTime == null;
    }
    // to string override
    @Override
    public String toString() {
        return "Session{" +
                "sessionId='" + sessionId + '\'' +
                ", userId=" + userId +
                ", sessionKey='" + sessionKey + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}