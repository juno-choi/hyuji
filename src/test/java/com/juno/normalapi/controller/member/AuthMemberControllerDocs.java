package com.juno.normalapi.controller.member;

import com.juno.normalapi.docs.DocsSupport;
import com.juno.normalapi.domain.dto.member.JoinMemberDto;
import com.juno.normalapi.domain.dto.member.LoginMemberDto;
import com.juno.normalapi.domain.entity.member.Member;
import com.juno.normalapi.domain.enums.JoinType;
import com.juno.normalapi.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthMemberControllerDocs extends DocsSupport {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private RedisTemplate<String, ?> redisTemplate;

    private final String URL = "/auth/member";
    private final String EMAIL = "docs@email.com";
    private final String PASSWORD = "test123!";

    @BeforeAll
    void setUp(){
        JoinMemberDto joinMemberDto = JoinMemberDto.builder()
                .email(EMAIL)
                .password(passwordEncoder.encode(PASSWORD))
                .name("테스터")
                .nickname("닉네임")
                .tel("01012341234")
                .zipCode("12345")
                .address("경기도 성남시 중원구")
                .addressDetail("상세 주소")
                .build();
        memberRepository.save(Member.of(joinMemberDto, JoinType.EMAIL));
    }

    @Test
    @DisplayName(URL+"/join")
    void join() throws Exception {
        JoinMemberDto joinMemberDto = JoinMemberDto.builder()
                .email("docs1@email.com")
                .password(PASSWORD)
                .name("테스터")
                .nickname("닉네임")
                .tel("01012341234")
                .zipCode("12345")
                .address("경기도 성남시 중원구")
                .addressDetail("상세 주소")
                .build();

        ResultActions perform = mock.perform(
                post(URL + "/join").contentType(MediaType.APPLICATION_JSON)
                .content(convertToString(joinMemberDto))
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

    @Test
    @DisplayName(URL+"/login")
    void login() throws Exception {
        LoginMemberDto loginMemberDto = LoginMemberDto.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        ResultActions perform = mock.perform(
                post(URL + "/login").contentType(MediaType.APPLICATION_JSON)
                        .content(convertToString(loginMemberDto))
        );

        String contentAsString = perform.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println(contentAsString);

        perform.andDo(docs.document(
                requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                ),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                        fieldWithPath("data.access_token").type(JsonFieldType.STRING).description("access token 1시간"),
                        fieldWithPath("data.refresh_token").type(JsonFieldType.STRING).description("refresh token 30일"),
                        fieldWithPath("data.access_token_expiration").type(JsonFieldType.NUMBER).description("access token 파기일 1시간"),
                        fieldWithPath("data.refresh_token_expiration").type(JsonFieldType.NUMBER).description("refresh token 파기일 30일")
                )
        ));
    }


    @Test
    @DisplayName(URL+"/refresh")
    void refresh() throws Exception {
        //given
        JoinMemberDto joinMemberDto = JoinMemberDto.builder()
                .email("refresh@mail.com")
                .password("test123!")
                .name("테스터")
                .nickname("닉네임")
                .tel("01012341234")
                .zipCode("12345")
                .address("경기도 성남시 중원구 자혜로17번길 16")
                .addressDetail("상세 주소")
                .build();
        Member member = memberRepository.save(Member.of(joinMemberDto, JoinType.EMAIL));

        String token = "refresh_token";
        redisTemplate.opsForHash().put(token, "refresh_token", String.valueOf(member.getId()));

        //when
        ResultActions perform = mock.perform(
                RestDocumentationRequestBuilders.get(URL + "/refresh/{token}", token).contentType(MediaType.APPLICATION_JSON)
        );
        //then
        perform.andDo(docs.document(
            pathParameters(
                parameterWithName("token").description("refresh token")
            ),
            responseFields(
                    fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
                    fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                    fieldWithPath("data.access_token").type(JsonFieldType.STRING).description("access token 1시간 (재발급)"),
                    fieldWithPath("data.access_token_expiration").type(JsonFieldType.NUMBER).description("access token 파기일 1시간")
            )
        ));
    }

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