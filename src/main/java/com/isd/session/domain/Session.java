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
    private String sessionId;

    @Column(name = "user_id")
    private Long userId;
    @Lob
    private byte[] data;
    @Column(name = "start_time")
    private Timestamp startTime;
    @Column(name = "end_time")
    private Timestamp endTime;

    // Getters and setters
}