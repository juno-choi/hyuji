package com.juno.normalapi.controller;

import com.juno.normalapi.docs.DocsSupport;
import com.juno.normalapi.domain.dto.RequestJoinMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerDocs extends DocsSupport {

    private final String URL = "/v1/member";

    @Test
    @DisplayName(URL+"/join")
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

        perform.andDo(docs.document(
            requestFields(
                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                fieldWithPath("tel").type(JsonFieldType.STRING).description("전화번호"),
                fieldWithPath("zip_code").type(JsonFieldType.STRING).description("우편번호"),
                fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
                fieldWithPath("address_detail").type(JsonFieldType.STRING).description("상세주소")
            ),
            responseFields(
                fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                fieldWithPath("data.member_id").type(JsonFieldType.NUMBER).description("회원 번호"),
                fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                fieldWithPath("data.name").type(JsonFieldType.STRING).description("이름"),
                fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("닉네임"),
                fieldWithPath("data.tel").type(JsonFieldType.STRING).description("전화번호"),
                fieldWithPath("data.zip_code").type(JsonFieldType.STRING).description("우편번호"),
                fieldWithPath("data.address").type(JsonFieldType.STRING).description("주소"),
                fieldWithPath("data.address_detail").type(JsonFieldType.STRING).description("상세주소"),
                fieldWithPath("data.role").type(JsonFieldType.STRING).description("회원 권한")
            )
        ));
    }

}