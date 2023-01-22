package com.juno.normalapi.service;

import com.juno.normalapi.domain.dto.RequestJoinMember;
import com.juno.normalapi.domain.entity.Member;
import com.juno.normalapi.domain.vo.JoinMember;
import com.juno.normalapi.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceImplTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입 성공")
    void join1(){
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
        JoinMember joinMember = memberService.join(requestJoinMember);
        List<Member> all = memberRepository.findAll();
        assertTrue(all.size() > 0);
    }
}