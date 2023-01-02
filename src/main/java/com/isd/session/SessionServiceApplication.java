package com.isd.session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@EnableWebMvc
@SpringBootApplication
public class SessionServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(SessionServiceApplication.class, args);
    }

}
