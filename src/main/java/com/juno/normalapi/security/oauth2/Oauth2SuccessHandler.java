package com.juno.normalapi.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juno.normalapi.api.Response;
import com.juno.normalapi.api.ResponseCode;
import com.juno.normalapi.domain.entity.member.Member;
import com.juno.normalapi.domain.vo.member.LoginMemberVo;
import com.juno.normalapi.repository.member.MemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final ObjectMapper objectMapper;
    private final Environment env;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        Member member = null;
        Long memberId = 0L;

        // TODO kakao, naver 등 sns oauth를 사용하여 id 가져와야함.
        if(registrationId.equals("kakao")){
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            Long snsId = (Long) oAuth2User.getAttributes().get("id");
            Map<String, Object> kakao_account = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
            member = memberRepository.findBySnsId(snsId).orElseThrow(()-> new IllegalArgumentException("유효하지 않은 회원입니다. 관리자에게 문의해주세요."));
        }
        memberId = member.getId();

        long accessExpirationEnv = Long.parseLong(env.getProperty("token.access.expiration"));
        long refreshExpirationEnv = Long.parseLong(env.getProperty("token.refresh.expiration"));

        Date accessTokenExpiration = new Date(System.currentTimeMillis() + (accessExpirationEnv * 1000L));
        Date refreshTokenExpiration = new Date(System.currentTimeMillis() + (refreshExpirationEnv * 1000L));

        Long accessTokenExpirationToLong = accessTokenExpiration.getTime();
        Long refreshTokenExpirationToLong = refreshTokenExpiration.getTime();

        // jwt 토큰 생성
        String memberIdAsString = String.valueOf(memberId);
        String accessToken = Jwts.builder()
                .setSubject(memberIdAsString)
                .setExpiration(accessTokenExpiration) //파기일
                .claim("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))    //암호화 알고리즘과 암호화 키값
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(memberIdAsString)
                .setExpiration(refreshTokenExpiration) //파기일
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))    //암호화 알고리즘과 암호화 키값
                .compact();

        // redis 토큰 등록
        log.info("redis token 등록");

        HashOperations<String, Object, Object> opsHash = redisTemplate.opsForHash();
        opsHash.put(accessToken, "access_token", memberIdAsString);
        opsHash.put(refreshToken, "refresh_token", memberIdAsString);
        redisTemplate.expire(accessToken, accessTokenExpirationToLong, TimeUnit.SECONDS);
        redisTemplate.expire(refreshToken, refreshTokenExpirationToLong, TimeUnit.SECONDS);

        LoginMemberVo loginMemberVo = LoginMemberVo.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiration(accessTokenExpirationToLong)
                .refreshTokenExpiration(refreshTokenExpirationToLong)
                .build();

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
