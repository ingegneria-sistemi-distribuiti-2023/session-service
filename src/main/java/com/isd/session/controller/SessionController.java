package com.isd.session.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.isd.session.commons.CurrencyEnum;
import com.isd.session.commons.OutcomeEnum;
import com.isd.session.dto.BetDTO;
import com.isd.session.dto.GameDTO;
import com.isd.session.dto.UserDataDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    @Autowired
    RedisTemplate<String, UserDataDTO> redisTemplate;

    // FIXME: this is a test method, remove it
    @PostMapping("/create_test")
    public ResponseEntity<UserDataDTO> createTestSession(
           /*  @RequestHeader(value = "Access-Token") String accessToken */) {
        // create a test session and return it
        // create a dummy UserDataDTO object
        UserDataDTO session = new UserDataDTO();
        session.setUserId(100);

        // create a list of dummy BetDTO instances and add them to the UserDataDTO listOfBets field
        GameDTO[]a = {
            new GameDTO(0, OutcomeEnum.DRAW, 0, 0),
            new GameDTO(1, OutcomeEnum.AWAY, 0, 0)
        };
        List<GameDTO> listOfGames = Arrays.asList(a);
        
        BetDTO bet1 = new BetDTO(100, CurrencyEnum.EUR, listOfGames, new Date().getTime());

        session.setListOfBets(Arrays.asList(bet1));

        // save the session to the redis database
        UUID uuid = UUID.randomUUID();
        redisTemplate.opsForValue().set(uuid.toString(), session);


        HttpHeaders headers = new HttpHeaders();
        headers.set("Session-Id", uuid.toString());
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create a new ResponseEntity with the headers and the response body.
        ResponseEntity<UserDataDTO> response = new ResponseEntity<>(session, headers, HttpStatus.OK);
        
        return response;
    }


    // @PostMapping(value="/session")
    // public UserDataDTO createAndUpdateSession(@RequestBody UserDataDTO entity, @RequestHeader(value = "Session-Id") String sessionId) {
        
    //     if(sessionId == null) {
    //         // generate a new session id if 
    //         sessionId = UUID.randomUUID().toString();
    //     }

    //     HttpHeaders headers = new HttpHeaders();
    //     headers.set("Session-Id", sessionId);
    //     headers.setContentType(MediaType.APPLICATION_JSON);

    //     // Create a new ResponseEntity with the headers and the response body.
    //     ResponseEntity<UserDataDTO> response = new ResponseEntity<>(session, headers, HttpStatus.OK);
        
    //     // Create a new ResponseEntity with the headers and the response body.
    //     ResponseEntity<UserDataDTO> response = new ResponseEntity<>(session, headers, HttpStatus.OK);
        
    //     return entity;
    }
}