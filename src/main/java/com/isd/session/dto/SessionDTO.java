package com.isd.session.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionDTO {
    private Integer sessionId;
    private Integer userId;
    private String sessionKey;
    private Timestamp startTime;
    private Timestamp endTime;
}



