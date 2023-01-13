package com.isd.session.auth;

import org.springframework.stereotype.Service;

/**
 * This service validate the accessToken sended from 'Access-Token' field.
 *
 * */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Override
    public boolean validateToken(String accessToken) {
        // Validate the access token using the database or any other method
        // TODO: implement this method
        // if (accessToken != null && accessToken.equals("pippo")){
        //     return true;
        // }

        // return false;
        return true;
    }
}
