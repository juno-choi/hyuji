package com.juno.normalapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private final ObjectMapper objectMapper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors().disable();
        http.headers().frameOptions().disable();
        http.formLogin().disable();
        http.httpBasic().disable();

        http.authorizeRequests().antMatchers("/**").permitAll()
        .and().addFilter(getAuthFilter());
    }

    // 로그인 요청시 filter
    private AuthFilter getAuthFilter() throws Exception {
        AuthFilter auth = new AuthFilter(objectMapper);
        auth.setAuthenticationManager(authenticationManager());
        auth.setFilterProcessesUrl("/hello/login");
        return auth;
    }
}
