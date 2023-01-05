package com.isd.session.auth;

public interface AuthenticationService {
    boolean validateToken(String accessToken);
}
