package com.juno.normalapi.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juno.normalapi.api.Response;
import com.juno.normalapi.api.ResponseCode;
import com.juno.normalapi.domain.vo.member.LoginMemberVo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthUtil {
    private final Environment env;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public void responseToken(HttpServletResponse response, Collection<? extends GrantedAuthority> authorities, Long memberId) throws IOException {
        String memberIdAsString = String.valueOf(memberId);

        // jwt 토큰 생성
        String accessToken = getAccessToken(authorities, memberIdAsString);
        Long accessTokenExpirationToLong = getAccessTokenExpiration();
        String refreshToken = getRefreshToken(memberIdAsString);
        Long refreshTokenExpirationToLong = getRefreshTokenExpiration();

        // redis 토큰 등록
        registerRedisToken(accessTokenExpirationToLong, refreshTokenExpirationToLong, memberIdAsString, accessToken, refreshToken);
        writeResponse(response, LoginMemberVo.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiration(accessTokenExpirationToLong)
                .refreshTokenExpiration(refreshTokenExpirationToLong)
                .build());
    }

    private Long getRefreshTokenExpiration() {
        long refreshExpirationEnv = Long.parseLong(env.getProperty("token.refresh.expiration"));
        Date refreshTokenExpiration = new Date(System.currentTimeMillis() + (refreshExpirationEnv * 1000L));
        return refreshTokenExpiration.getTime();
    }

    private String getRefreshToken(String memberIdAsString) {
        long refreshExpirationEnv = Long.parseLong(env.getProperty("token.refresh.expiration"));
        Date refreshTokenExpiration = new Date(System.currentTimeMillis() + (refreshExpirationEnv * 1000L));

        return Jwts.builder()
                .setSubject(memberIdAsString)
                .setExpiration(refreshTokenExpiration) //파기일
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))    //암호화 알고리즘과 암호화 키값
                .compact();
    }

    private Long getAccessTokenExpiration() {
        long accessExpirationEnv = Long.parseLong(env.getProperty("token.access.expiration"));
        Date accessTokenExpiration = new Date(System.currentTimeMillis() + (accessExpirationEnv * 1000L));
        return accessTokenExpiration.getTime();
    }

    private String getAccessToken(Collection<? extends GrantedAuthority> authorities, String memberIdAsString) {
        long accessExpirationEnv = Long.parseLong(env.getProperty("token.access.expiration"));
        Date accessTokenExpiration = new Date(System.currentTimeMillis() + (accessExpirationEnv * 1000L));

        return Jwts.builder()
                .setSubject(memberIdAsString)
                .setExpiration(accessTokenExpiration) //파기일
                .claim("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))    //암호화 알고리즘과 암호화 키값
                .compact();
    }

    private void registerRedisToken(Long accessTokenExpirationToLong, Long refreshTokenExpirationToLong, String memberIdAsString, String accessToken, String refreshToken) {
        HashOperations<String, Object, Object> opsHash = redisTemplate.opsForHash();
        opsHash.put(accessToken, "access_token", memberIdAsString);
        opsHash.put(refreshToken, "refresh_token", memberIdAsString);
        redisTemplate.expire(accessToken, accessTokenExpirationToLong, TimeUnit.MILLISECONDS);
        redisTemplate.expire(refreshToken, refreshTokenExpirationToLong, TimeUnit.MILLISECONDS);
    }

    private void writeResponse(HttpServletResponse response, LoginMemberVo loginMemberVo) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        Response<LoginMemberVo> responseDto = Response.<LoginMemberVo>builder()
                .code(ResponseCode.SUCCESS)
                .message("로그인 성공")
                .data(loginMemberVo)
                .build();
        writer.write(objectMapper.writeValueAsString(responseDto));
    }
}
