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
import com.isd.session.dto.MatchGambledDTO;
import com.isd.session.dto.UserDataDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;



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
        MatchGambledDTO[]a = {
            new MatchGambledDTO(0, OutcomeEnum.DRAW, 0, 0),
            new MatchGambledDTO(1, OutcomeEnum.AWAY, 0, 0)
        };
        List<MatchGambledDTO> listOfGames = Arrays.asList(a);
        
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


    @PostMapping(value="/")
    public UserDataDTO createAndUpdateSession(@RequestBody UserDataDTO entity, @RequestHeader(value = "Session-Id", required = false) String headerSessionId) {
        // if the session id is provided in the header, update the session
        // TODO: aggiornare scadenza sessione

        String sessionId = headerSessionId;
        if(headerSessionId == null) {
            // generate a new session id if a new session is being created
            sessionId = UUID.randomUUID().toString();
        }

        // save the session to the redis database
        redisTemplate.opsForValue().set(sessionId, entity);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Session-Id", sessionId);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create a new ResponseEntity with the headers and the response body.
        ResponseEntity<UserDataDTO> response = new ResponseEntity<>(entity, headers, HttpStatus.OK);
        
        return response.getBody();
    }


    @GetMapping(value="/{id}")
    public UserDataDTO getSession(@PathVariable("id") String id) {
        // TODO: aggiornare scadenza sessione
        
        if(id == null) {
            // return 404 error if the session id is not provided
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
            );
        }
        // get the session from the redis database
        UserDataDTO session = redisTemplate.opsForValue().get(id);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Session-Id", id);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create a new ResponseEntity with the headers and the response body.
        ResponseEntity<UserDataDTO> response = new ResponseEntity<>(session, headers, HttpStatus.OK);
        
        return response.getBody();
    }
}