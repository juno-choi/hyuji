package com.juno.normalapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juno.normalapi.domain.dto.RequestJoinMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

        ResultActions perform = mockMvc.perform(
                post(URL + "/join").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestJoinMember))
        );
        perform.andDo(print());
    }

    @Test
    @DisplayName("로그인 테스트")
    void loginTest1() throws Exception {
        ResultActions perform = mockMvc.perform(get(URL + "/login"));
        perform.andDo(print());
    }
}