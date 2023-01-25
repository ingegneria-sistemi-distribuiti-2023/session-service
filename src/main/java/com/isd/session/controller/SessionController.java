package com.isd.session.controller;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SessionController {
    private final SessionService sessionService;

    @PostMapping(value="/")
    public ResponseEntity<UserDataDTO> createAndUpdateSession(@RequestHeader("Secret-Key") String secretKey, @RequestBody UserDataDTO entity) {
        return new ResponseEntity<UserDataDTO>(sessionService.createAndUpdateSession(entity), HttpStatus.OK);
    }

    @GetMapping(value="/{userId}")
    public ResponseEntity<UserDataDTO> getSession(@RequestHeader("Secret-Key") String secretKey, @PathVariable("userId") Integer userId) {
        return new ResponseEntity<UserDataDTO>(sessionService.getSessionByUserId(userId), HttpStatus.OK);

    }
}