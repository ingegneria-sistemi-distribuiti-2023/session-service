package com.isd.session.controller;

import com.isd.session.dto.SessionDTO;
import com.isd.session.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @GetMapping
    public ResponseEntity<List<SessionDTO>> getAllSessions(
            @RequestHeader(value = "Access-Token") String accessToken) {
        List<SessionDTO> sessions = sessionService.getAllSessions();
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionDTO> getSessionById(
            @RequestHeader(value = "Access-Token") String accessToken,
            @PathVariable("id") String sessionId) {
        SessionDTO session = sessionService.getSessionById(sessionId);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(session);
    }
}