package com.juno.normalapi.controller;

import com.juno.normalapi.docs.TestSupport;
import com.juno.normalapi.domain.dto.RequestJoinMember;
import com.juno.normalapi.domain.entity.Member;
import com.juno.normalapi.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest extends TestSupport {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    private final String URL = "/v1/member";

    @Test
    @DisplayName("회원 가입 성공")
    void joinMemberSuccess() throws Exception {
        RequestJoinMember requestJoinMember = RequestJoinMember.builder()
                .email("test@naver.com")
                .password("test123!")
                .name("테스터")
                .nickname("닉네임")
                .tel("01012341234")
                .zipCode("12345")
                .address("경기도 성남시 중원구 자혜로17번길 16")
                .addressDetail("상세 주소")
                .build();

        ResultActions perform = mock.perform(
                post(URL + "/join").contentType(MediaType.APPLICATION_JSON)
                .content(convertToString(requestJoinMember))
        );
        perform.andDo(print());
    }

    @Test
    @DisplayName("로그인 성공")
    void loginMemberSuccess1() throws Exception {
        String email = "test@test.com";
        String password = "password";
        Map<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("password", password);

        Member member = Member.builder()
                .email(email)
                .password(password)
                .name("tester")
                .nickname("테스터")
                .role("TESTER")
                .build();
        member.encryptPassword(member, passwordEncoder);
        memberRepository.save(member);

        ResultActions perform = mock.perform(
                post(URL + "/login").contentType(MediaType.APPLICATION_JSON)
                        .content(convertToString(map))
        ).andDo(print());
    }
}