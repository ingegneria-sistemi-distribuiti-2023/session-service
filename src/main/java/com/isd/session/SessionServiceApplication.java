package com.isd.session;

import com.isd.session.auth.AuthenticationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@EnableWebMvc
@SpringBootApplication
public class SessionServiceApplication {

    public static void main(final String[] args) {
        SpringApplication.run(SessionServiceApplication.class, args);
    }

    /**
     * Register custom filter on SessionService
     * */
    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilterFilterRegistrationBean(){
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthenticationFilter());
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }

}
