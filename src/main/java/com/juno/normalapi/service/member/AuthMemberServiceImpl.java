package com.juno.normalapi.service.member;

import com.juno.normalapi.domain.dto.member.JoinMemberDto;
import com.juno.normalapi.domain.entity.member.Member;
import com.juno.normalapi.domain.enums.JoinType;
import com.juno.normalapi.domain.vo.member.JoinMemberVo;
import com.juno.normalapi.domain.vo.member.LoginMemberVo;
import com.juno.normalapi.repository.member.MemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthMemberServiceImpl implements AuthMemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisTemplate<String, ?> redisTemplate;
    private final Environment env;

    @Transactional
    @Override
    public JoinMemberVo join(JoinMemberDto joinMemberDto) {
        Optional<Member> findMember = memberRepository.findByEmail(joinMemberDto.getEmail());
        if(findMember.isPresent()){
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
        }

        Member member = Member.of(joinMemberDto, JoinType.EMAIL);
        member.encryptPassword(member, passwordEncoder);
        Member saveMember = memberRepository.save(member);
        return JoinMemberVo.of(saveMember);
    }

    @Override
    public LoginMemberVo refresh(String token) {
        String memberIdStr = (String) redisTemplate.opsForHash().get(token, "refresh_token");
        if(memberIdStr == null){
            throw new IllegalArgumentException("토큰 값이 유효하지 않습니다.");
        }

        Member findMember = memberRepository.findById(Long.valueOf(memberIdStr)).orElseThrow(
            () -> new IllegalArgumentException("유효하지 않은 회원입니다. 관리자에게 문의해주세요!")
        );

        Long memberId = findMember.getId();
        String roles = findMember.getRole();
        String[] authorities = roles.split(",");

        String accessExpirationEnv = env.getProperty("token.access.expiration");
        Date accessExpiration = new Date(System.currentTimeMillis() + (Long.parseLong(accessExpirationEnv) * 1000L));
        Long accessTokenExpirationToLong = accessExpiration.getTime();

        // jwt 토큰 생성
        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .setExpiration(accessExpiration) //파기일
                .claim("roles", Arrays.stream(authorities).collect(Collectors.toList()))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))    //암호화 알고리즘과 암호화 키값
                .compact();

        redisTemplate.opsForHash().put(accessToken, "access_token", String.valueOf(memberId));
        redisTemplate.expire(accessToken, accessTokenExpirationToLong, TimeUnit.MILLISECONDS);

        LoginMemberVo loginMemberVo = LoginMemberVo.builder()
                .accessToken(accessToken)
                .accessTokenExpiration(accessTokenExpirationToLong)
                .build();

        return loginMemberVo;
    }


}
