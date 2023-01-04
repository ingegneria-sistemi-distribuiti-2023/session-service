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
    private Integer userId;
    @Lob
    private byte[] data;
    @Column(name = "start_time")
    private Timestamp startTime;
    @Column(name = "end_time")
    private Timestamp endTime;

    public Session() {
    }

    @Override
    public boolean equals(Object obj) {
        boolean same = false;

        if (obj != null && obj instanceof Session){
            same = this.sessionId == ((Session) obj).getSessionId() &&
                    this.userId == ((Session) obj).getUserId() &&
                    this.data == ((Session) obj).getData() &&
                    this.startTime == ((Session) obj).getStartTime() &&
                    this.endTime == ((Session) obj).getEndTime();
        }

        return same;
    }

}