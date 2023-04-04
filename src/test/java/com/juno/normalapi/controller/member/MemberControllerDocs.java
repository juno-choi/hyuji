package com.juno.normalapi.controller.member;

import com.juno.normalapi.docs.DocsSupport;
import com.juno.normalapi.domain.dto.member.JoinMemberDto;
import com.juno.normalapi.domain.entity.member.Member;
import com.juno.normalapi.domain.enums.JoinType;
import com.juno.normalapi.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerDocs extends DocsSupport {
    @Autowired
    private MemberRepository memberRepository;

    private final String URL = "/v1/member";

    @Test
    @DisplayName(URL+"/{member_id} (GET)")
    void getMember() throws Exception {
        // given
        JoinMemberDto joinMemberDto = JoinMemberDto.builder()
                .email("get@mail.com")
                .password("test123!")
                .name("조회")
                .nickname("검색되는")
                .tel("01012341234")
                .zipCode("12345")
                .address("경기도 성남시 중원구 자혜로17번길 16")
                .addressDetail("상세 주소")
                .build();
        Member saveMember = memberRepository.save(Member.of(joinMemberDto, JoinType.EMAIL));
        // when
        ResultActions perform = mock.perform(
                RestDocumentationRequestBuilders.get(URL + "/{member_id}", saveMember.getId())
                        .header(AUTHORIZATION, ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        perform.andDo(docs.document(
                pathParameters(
                        parameterWithName("member_id").description("조회할 회원 id")
                ),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("회원 id"),
                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("회원 email"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("회원 이름"),
                        fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                        fieldWithPath("data.tel").type(JsonFieldType.STRING).description("회원 전화번호"),
                        fieldWithPath("data.join_type").type(JsonFieldType.STRING).description("회원 가입 경로"),
                        fieldWithPath("data.zip_code").type(JsonFieldType.STRING).description("회원 우편번호"),
                        fieldWithPath("data.address").type(JsonFieldType.STRING).description("회원 주소"),
                        fieldWithPath("data.address_detail").type(JsonFieldType.STRING).description("회원 상세 주소"),
                        fieldWithPath("data.created_at").type(JsonFieldType.STRING).description("회원 가입일"),
                        fieldWithPath("data.modified_at").type(JsonFieldType.STRING).description("회원 수정일")

                )
        ));
    }
}