package com.juno.normalapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juno.normalapi.security.oauth2.Oauth2SuccessHandler;
import com.juno.normalapi.security.oauth2.Oauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private final ObjectMapper objectMapper;
    private final AuthService authService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Environment env;
    private final RedisTemplate<String, ?> redisTemplate;
    private final Oauth2SuccessHandler oauth2SuccessHandler;
    private final Oauth2UserService oauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.cors().disable();
        http.headers().frameOptions().disable();
        http.formLogin().disable();
        http.httpBasic().disable();

        http.oauth2Login()
                .successHandler(oauth2SuccessHandler) // 성공 핸들링
                .userInfoEndpoint()
                .userService(oauth2UserService);    // 성공 후처리 service ex) 로그인 처리나 회원가입 진행

        http.authorizeRequests().antMatchers("/**").permitAll()
        .and().addFilter(getAuthFilter());
    }

    // 로그인 요청시 filter
    private AuthFilter getAuthFilter() throws Exception {
        AuthFilter filter = new AuthFilter(env, objectMapper, redisTemplate);
        filter.setAuthenticationManager(authenticationManager());
        filter.setFilterProcessesUrl("/auth/member/login");
        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authService).passwordEncoder(passwordEncoder);
    }
}
