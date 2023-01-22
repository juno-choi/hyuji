package com.juno.normalapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juno.normalapi.api.Response;
import com.juno.normalapi.api.ResponseCode;
import com.juno.normalapi.domain.dto.EmptyDto;
import com.juno.normalapi.domain.dto.RequestLoginMember;
import com.juno.normalapi.domain.entity.Member;
import com.juno.normalapi.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class AuthFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;
    private final Environment env;

    // 인증 요청을 보냈을 경우
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("인증 요청");
        RequestLoginMember requestLoginMember = null;
        try {
            requestLoginMember = objectMapper.readValue(request.getInputStream(), RequestLoginMember.class);
        } catch (IOException e) {
            log.error("로그인 요청 양식 에러", e);
            onError(response, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러", "서버 내부 에러");
            return null;
        }

        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(requestLoginMember.getEmail(), requestLoginMember.getPassword(), new ArrayList<>())
        );
    }

    // 인증에 성공 했을 경우
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("인증 성공");
        // member db 조회
        User user = (User) authResult.getPrincipal();
        String memberId = user.getUsername();
        //권한
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        log.debug("memberId = {}", memberId);

        // jwt 토큰 생성
        String accessToken = Jwts.builder().setSubject(memberId).setExpiration(new Date(System.currentTimeMillis() + (Long.parseLong(env.getProperty("token.access.expiration")) * 1000L))) //파기일
                .claim("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))    //암호화 알고리즘과 암호화 키값
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(memberId)
                .setExpiration(new Date(System.currentTimeMillis() + (Long.parseLong(env.getProperty("token.refresh.expiration")) * 1000L))) //파기일
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))    //암호화 알고리즘과 암호화 키값
                .compact();

        Map<String, String> map = new HashMap<>();
        map.put("access_token", accessToken);
        map.put("refresh_token", refreshToken);

        // redis 토큰 등록

        // 반환 정보 생성
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        PrintWriter writer = response.getWriter();
        Response<Map> responseDto = Response.<Map>builder()
                .code(ResponseCode.SUCCESS)
                .message("로그인 성공")
                .data(map)
                .build();
        writer.write(objectMapper.writeValueAsString(responseDto));
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

    private void onError(HttpServletResponse response, HttpStatus httpStatus, String message, String dataMessage) {
        try {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setStatus(httpStatus.value());
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
