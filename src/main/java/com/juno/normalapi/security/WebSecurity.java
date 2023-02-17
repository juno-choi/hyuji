package com.juno.normalapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private final ObjectMapper objectMapper;
    private final AuthService authService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Environment env;
    private final RedisTemplate<String, ?> redisTemplate;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors().disable();
        http.headers().frameOptions().disable();
        http.formLogin().disable();
        http.httpBasic().disable();

        http.authorizeRequests().antMatchers("/**").permitAll()
        .and().addFilter(getAuthFilter())
        ;
    }

    // 로그인 요청시 filter
    private AuthFilter getAuthFilter() throws Exception {
        AuthFilter auth = new AuthFilter(env, objectMapper, redisTemplate);
        auth.setAuthenticationManager(authenticationManager());
        auth.setFilterProcessesUrl("/auth/member/login");
        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authService).passwordEncoder(passwordEncoder);
    }
}
