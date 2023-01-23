package com.juno.normalapi.service;

import com.juno.normalapi.domain.dto.RequestJoinMember;
import com.juno.normalapi.domain.entity.Member;
import com.juno.normalapi.domain.enums.JoinType;
import com.juno.normalapi.domain.vo.JoinMember;
import com.juno.normalapi.domain.vo.LoginMember;
import com.juno.normalapi.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisTemplate<String, ?> redisTemplate;
    private final Environment env;

    @Transactional
    @Override
    public JoinMember join(RequestJoinMember requestJoinMember) {
        Member member = Member.of(requestJoinMember, JoinType.EMAIL);
        member.encryptPassword(member, passwordEncoder);
        Member saveMember = memberRepository.save(member);
        return JoinMember.of(saveMember);
    }

    @Override
    public LoginMember refresh(String token) {
        String memberIdStr = (String) redisTemplate.opsForHash().get(token, "refresh_token");
        if(memberIdStr == null){
            throw new IllegalArgumentException("토큰 값이 유효하지 않습니다.");
        }

        Member findMember = memberRepository.findById(Long.valueOf(memberIdStr)).orElseThrow(
                () -> new IllegalArgumentException("유효하지 않은 회원입니다. 관리자에게 문의해주세요!")
        );

        Long memberId = findMember.getMemberId();
        String roles = findMember.getRole();
        String[] authorities = roles.split(",");
        // jwt 토큰 생성
        String accessToken = Jwts.builder().setSubject(String.valueOf(memberId)).setExpiration(new Date(System.currentTimeMillis() + (Long.parseLong(env.getProperty("token.access.expiration")) * 1000L))) //파기일
                .claim("roles", Arrays.stream(authorities).collect(Collectors.toList()))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))    //암호화 알고리즘과 암호화 키값
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .setExpiration(new Date(System.currentTimeMillis() + (Long.parseLong(env.getProperty("token.refresh.expiration")) * 1000L))) //파기일
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))    //암호화 알고리즘과 암호화 키값
                .compact();

        LoginMember loginMember = LoginMember.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return loginMember;
    }
}
