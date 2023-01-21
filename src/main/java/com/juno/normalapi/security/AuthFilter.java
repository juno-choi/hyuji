package com.juno.normalapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juno.normalapi.api.Response;
import com.juno.normalapi.api.ResponseCode;
import com.juno.normalapi.domain.EmptyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public class AuthFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper;

    // 인증 요청을 보냈을 경우
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("인증 요청");
        return super.attemptAuthentication(request, response);
    }

    // 인증에 성공 했을 경우
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("인증 성공");
        // 성공시 jwt 토큰 발급해주면 됨!
        super.successfulAuthentication(request, response, chain, authResult);
    }

    // 인증에 실패했을 때
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        final String MESSAGE = "로그인 실패";
        log.error(MESSAGE);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        PrintWriter writer = response.getWriter();
        Response<EmptyDto> responseDto = Response.<EmptyDto>builder()
                .code(ResponseCode.UNAUTHORIZED)
                .message(MESSAGE)
                .data(EmptyDto.of(MESSAGE))
                .build();

        writer.write(objectMapper.writeValueAsString(responseDto));
    }
}
