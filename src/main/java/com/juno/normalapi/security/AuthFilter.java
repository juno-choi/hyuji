package com.juno.normalapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juno.normalapi.api.Response;
import com.juno.normalapi.api.ResponseCode;
import com.juno.normalapi.domain.dto.EmptyDto;
import com.juno.normalapi.domain.dto.member.RequestLoginMember;
import com.juno.normalapi.domain.vo.member.LoginMember;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class AuthFilter extends UsernamePasswordAuthenticationFilter {
    private final Environment env;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, ?> redisTemplate;


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

        long accessExpirationEnv = Long.parseLong(env.getProperty("token.access.expiration"));
        long refreshExpirationEnv = Long.parseLong(env.getProperty("token.refresh.expiration"));

        Date accessTokenExpiration = new Date(System.currentTimeMillis() + (accessExpirationEnv * 1000L));
        Date refreshTokenExpiration = new Date(System.currentTimeMillis() + (refreshExpirationEnv * 1000L));

        Long accessTokenExpirationToLong = accessTokenExpiration.getTime();
        Long refreshTokenExpirationToLong = refreshTokenExpiration.getTime();

        // jwt 토큰 생성
        String accessToken = Jwts.builder()
                .setSubject(memberId)
                .setExpiration(accessTokenExpiration) //파기일
                .claim("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))    //암호화 알고리즘과 암호화 키값
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(memberId)
                .setExpiration(refreshTokenExpiration) //파기일
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))    //암호화 알고리즘과 암호화 키값
                .compact();

        // redis 토큰 등록
        log.info("redis token 등록");

        HashOperations<String, Object, Object> opsHash = redisTemplate.opsForHash();
        opsHash.put(accessToken, "access_token", memberId);
        opsHash.put(refreshToken, "refresh_token", memberId);
        redisTemplate.expire(accessToken, accessTokenExpirationToLong, TimeUnit.SECONDS);
        redisTemplate.expire(refreshToken, refreshTokenExpirationToLong, TimeUnit.SECONDS);

        LoginMember loginMember = LoginMember.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiration(accessTokenExpirationToLong)
                .refreshTokenExpiration(refreshTokenExpirationToLong)
                .build();

        // 반환 정보 생성
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        Response<LoginMember> responseDto = Response.<LoginMember>builder()
                .code(ResponseCode.SUCCESS)
                .message("로그인 성공")
                .data(loginMember)
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
