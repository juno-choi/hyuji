package com.juno.normalapi.service;

import com.juno.normalapi.domain.dto.RequestJoinMember;
import com.juno.normalapi.domain.entity.Member;
import com.juno.normalapi.domain.vo.JoinMember;
import com.juno.normalapi.domain.vo.LoginMember;
import com.juno.normalapi.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceImplTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RedisTemplate<String, ?> redisTemplate;

    @Test
    @DisplayName("회원가입 성공")
    void joinSuccess1(){
        // given
        RequestJoinMember requestJoinMember = RequestJoinMember.builder()
                .email("test2@naver.com")
                .password("test123!")
                .name("테스터")
                .nickname("닉네임")
                .tel("01012341234")
                .zipCode("12345")
                .address("경기도 성남시 중원구 자혜로17번길 16")
                .addressDetail("상세 주소")
               .build();
        // when
        JoinMember joinMember = memberService.join(requestJoinMember);

        // then
        List<Member> all = memberRepository.findAll();
        assertTrue(all.size() > 0);
    }

    @Test
    @DisplayName("유효하지 않은 토큰은 실패한다.")
    void refreshFail1(){
        // given & when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> memberService.refresh("invalid_token")
        );

        // then
        assertEquals("토큰 값이 유효하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("유효하지 않은 회원은 실패한다.")
    void refreshFail2(){
        // given
        String token = "temp_token";
        redisTemplate.opsForHash().put(token, "refresh_token", "0");

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> memberService.refresh(token)
        );

        // then
        assertEquals("유효하지 않은 회원입니다. 관리자에게 문의해주세요!", exception.getMessage());
    }

    @Test
    @DisplayName("토큰 재발급에 성공한다.")
    void refreshSuccess1(){
        // given
        RequestJoinMember requestJoinMember = RequestJoinMember.builder()
                .email("refresh@naver.com")
                .password("test123!")
                .name("테스터")
                .nickname("닉네임")
                .tel("01012341234")
                .zipCode("12345")
                .address("경기도 성남시 중원구 자혜로17번길 16")
                .addressDetail("상세 주소")
                .build();
        JoinMember joinMember = memberService.join(requestJoinMember);

        String token = "token";
        redisTemplate.opsForHash().put(token, "refresh_token", String.valueOf(joinMember.getMemberId()));

        // when
        LoginMember loginMember = memberService.refresh(token);

        // then
        assertNotNull(loginMember.getAccessToken());
    }
}