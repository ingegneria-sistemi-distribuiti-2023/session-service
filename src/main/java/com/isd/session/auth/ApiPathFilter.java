package com.isd.session.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiPathFilter extends OncePerRequestFilter {
    @Value("${app.service.secret}")
    private String SECRET_APP;
    private final static String SECRET_HEADER = "Secret-Key";

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiPathFilter.class);

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String secretFromApp = request.getHeader(SECRET_HEADER);

        if (!SECRET_APP.equals(secretFromApp)){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);

    }
}
