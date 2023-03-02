package com.juno.normalapi.interceptor;

import com.juno.normalapi.domain.dto.RequestJoinMember;
import com.juno.normalapi.domain.entity.Member;
import com.juno.normalapi.domain.enums.JoinType;
import com.juno.normalapi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Optional;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
public class TokenInterceptor implements HandlerInterceptor {
    private final Environment env;
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberRepository memberRepository;
    private final String TEST_ACCESS_TOKEN = "test";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("TokenInterceptor start...");

        // local에서 테스트 진행할 경우 예외
        String profile = env.getProperty("spring.profiles.active");
        String accessToken = request.getHeader(AUTHORIZATION);
        Optional.ofNullable(accessToken).orElseThrow(() -> new AuthenticationException("please check header token value"));
        accessToken = accessToken.replace("Bearer ", "");

        if(profile.equals("local") && accessToken.equals(TEST_ACCESS_TOKEN)){
            // 테스트를 위한 회원 계정 지정해두자!
            String testEmail = env.getProperty("normal.test.email");

            Optional<Member> findMember = memberRepository.findByEmail(testEmail);
            if(findMember.isEmpty()){
                makeTestMemberProcess(request, testEmail);
                return true;
            }
            Long memberId = findMember.get().getMemberId();
            request.setAttribute("loginUserId", memberId);
            return true;
        }

        String loginUserTokenValue = (String) redisTemplate.opsForHash().get(accessToken, "access_token");
        Optional.ofNullable(loginUserTokenValue).orElseThrow(()-> new AuthenticationException("유효하지 않은 access token 입니다."));
        Long loginUserId = Long.parseLong(loginUserTokenValue);

        request.setAttribute("loginUserId", loginUserId);
        return true;
    }

    private void makeTestMemberProcess(HttpServletRequest request, String testEmail) {
        Member saveMember = memberRepository.save(
                Member.of(RequestJoinMember.builder()
                        .name("테스터")
                        .address("주소")
                        .addressDetail("상세주소")
                        .email(testEmail)
                        .nickname("테스터닉네임")
                        .tel("01012341234")
                        .zipCode("우편번호")
                        .password("qwer1234!")
                        .build(), JoinType.EMAIL));

        Long memberId = saveMember.getMemberId();
        request.setAttribute("loginUserId", memberId);

        redisTemplate.opsForHash().put(TEST_ACCESS_TOKEN, "access_token", String.valueOf(memberId));
    }
}
