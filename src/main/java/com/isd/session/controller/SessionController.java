package com.isd.session.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.isd.session.dto.UserDataDTO;
import com.isd.session.service.SessionService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/session")
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping(value="/")
    public ResponseEntity<UserDataDTO> createAndUpdateSession(@RequestBody UserDataDTO entity) {
        return new ResponseEntity<UserDataDTO>(sessionService.createAndUpdateSession(entity), HttpStatus.OK);
    }

    @GetMapping(value="/{userId}")
    public ResponseEntity<UserDataDTO> getSession(@PathVariable("userId") Integer userId) {
        return new ResponseEntity<UserDataDTO>(sessionService.getSessionByUserId(userId), HttpStatus.OK);

    }
}