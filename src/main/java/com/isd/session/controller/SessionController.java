package com.isd.session.controller;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.isd.session.dto.SessionDTO;
import com.isd.session.dto.UserDataDTO;
import com.isd.session.service.SessionService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;



@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private static int EXPIRE_DELTA = 24; // TODO: move to properties

    @Autowired
    RedisTemplate<String, UserDataDTO> redisTemplate;

    @Autowired
    SessionService sessionService;

    @PostMapping(value="/")
    public UserDataDTO createAndUpdateSession(@RequestBody UserDataDTO entity) {
        // TODO: refactor this method
        // check if a session ID is present in the database using the sessionService
        SessionDTO userSession = sessionService.getByUserId(entity.getUserId());
        String sessionKey = null;

        // the expire timestamp is calculated
        Long expireTimestamp = new Date().getTime() + TimeUnit.HOURS.toMillis(EXPIRE_DELTA);
        // if the session does not exist, a new session is created
        // the session is creades also if it's present and the expire timestamp is expired
        // the old session will be deleted
        if(userSession == null || userSession.getEndTime().getTime() < new Date().getTime()) {
            // if the session exists, it is deleted
            if(userSession != null) {
                sessionService.deleteSession(userSession.getSessionKey());
                // the data stored in the redis database is deleted
                redisTemplate.delete(userSession.getSessionKey());
            }
            SessionDTO sessionDTO = new SessionDTO();
            sessionDTO.setUserId(entity.getUserId());
            sessionDTO.setStartTime(new Timestamp(new Date().getTime()));
            sessionDTO.setEndTime(new Timestamp(expireTimestamp));
            // generate a new session id if a new session is being created
            sessionKey = UUID.randomUUID().toString();
            entity.setSessionId(sessionKey);
            sessionDTO.setSessionKey(sessionKey);
            sessionService.createSession(sessionDTO);
        } else {
            sessionKey = userSession.getSessionKey();
            // if the session exists, the expire timestamp is updated
            userSession.setEndTime(new Timestamp(expireTimestamp));
            sessionService.updateSession(userSession);
        }

        // save the session to the redis database
        redisTemplate.opsForValue().set(sessionKey, entity);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Session-Id", sessionKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create a new ResponseEntity with the headers and the response body.
        ResponseEntity<UserDataDTO> response = new ResponseEntity<>(entity, headers, HttpStatus.OK);
        
        return response.getBody();
    }


    @GetMapping(value="/{userId}")
    public UserDataDTO getSession(@PathVariable("userId") String userId) {
        // TODO: refactor this method
        // check if a session ID is present in the database using the sessionService
        SessionDTO userSession = sessionService.getByUserId(Integer.parseInt(userId));

        // the expire timestamp is calculated
        Long expireTimestamp = new Date().getTime() + TimeUnit.HOURS.toMillis(EXPIRE_DELTA);
        // if it's expired the session is deleted
        if (userSession != null && userSession.getEndTime().getTime() < new Date().getTime()) {
            sessionService.deleteSession(userSession.getSessionKey());
            // the data stored in the redis database is deleted
            // TODO: to be confirmed
            redisTemplate.delete(userSession.getSessionKey());
            userSession = null;
        }
        if (userSession == null) {
            // if the session does not exist or it's expired, an 404 error is returned 
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
            );
        } else {
            // if the session exists, the expire timestamp is updated
            userSession.setEndTime(new Timestamp(expireTimestamp));
            sessionService.updateSession(userSession);
        }

        // get the session from the redis database
        UserDataDTO session = redisTemplate.opsForValue().get(userSession.getSessionKey());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create a new ResponseEntity with the headers and the response body.
        ResponseEntity<UserDataDTO> response = new ResponseEntity<>(session, headers, HttpStatus.OK);
        
        return response.getBody();
    }
}