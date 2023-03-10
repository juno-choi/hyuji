package com.juno.normalapi.controller.board;

import com.juno.normalapi.docs.DocsSupport;
import com.juno.normalapi.domain.dto.RequestBoard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class BoardControllerDocs extends DocsSupport {
    private final String URL = "/v1/board";

    @Test
    @DisplayName(URL + " (POST)")
    void postBoard() throws Exception {
        //given
        RequestBoard requestBoard = RequestBoard.builder().title("테스트 글").content("테스트 내용").build();
        //when
        ResultActions perform = mock.perform(post(URL).header(AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertToString(requestBoard))
        ).andDo(print());
        //then
        perform.andDo(docs.document(
                requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("내용")
                ),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                        fieldWithPath("data.board_id").type(JsonFieldType.NUMBER).description("게시글 번호"),
                        fieldWithPath("data.member_id").type(JsonFieldType.NUMBER).description("회원 번호"),
                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("제목"),
                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("내용"),
                        fieldWithPath("data.writer").type(JsonFieldType.STRING).description("닉네임"),
                        fieldWithPath("data.created_at").type(JsonFieldType.STRING).description("등록일")
                )
        ));
    }
}