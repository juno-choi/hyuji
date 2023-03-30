package com.juno.normalapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juno.normalapi.api.Response;
import com.juno.normalapi.api.ResponseCode;
import com.juno.normalapi.domain.dto.EmptyDto;
import com.juno.normalapi.domain.dto.member.LoginMemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class AuthFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper;
    private final AuthUtil authUtil;

    // 인증 요청을 보냈을 경우
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("인증 요청");
        LoginMemberDto loginMemberDto = null;
        try {
            loginMemberDto = objectMapper.readValue(request.getInputStream(), LoginMemberDto.class);
        } catch (IOException e) {
            log.error("로그인 요청 양식 에러", e);
            onError(response, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러", "서버 내부 에러");
            return null;
        }

        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(loginMemberDto.getEmail(), loginMemberDto.getPassword(), new ArrayList<>())
        );
    }

    // 인증에 성공 했을 경우
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // member db 조회
        User user = (User) authResult.getPrincipal();
        Long memberId = Long.valueOf(user.getUsername());
        //권한
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        log.debug("memberId = {}", memberId);
        log.info("인증 성공 [memberId = {}]", memberId);

        authUtil.responseToken(response, authorities, memberId);
    }

    // 인증에 실패했을 때
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        final String MESSAGE = "로그인 실패";
        log.error(MESSAGE);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        Response<EmptyDto> responseDto = Response.<EmptyDto>builder()
                .code(ResponseCode.UNAUTHORIZED)
                .message(MESSAGE)
                .data(EmptyDto.of(MESSAGE))
                .build();

        writer.write(objectMapper.writeValueAsString(responseDto));
    }

    private void onError(HttpServletResponse response, HttpStatus httpStatus, String message, String dataMessage) {
        try {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setStatus(httpStatus.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter writer = response.getWriter();
            Response<EmptyDto> responseDto = Response.<EmptyDto>builder()
                    .code(ResponseCode.FAIL)
                    .message(message)
                    .data(EmptyDto.of(dataMessage))
                    .build();
            writer.write(objectMapper.writeValueAsString(responseDto));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
