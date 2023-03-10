package com.juno.normalapi.controller.board;

import com.juno.normalapi.docs.TestSupport;
import com.juno.normalapi.domain.dto.RequestBoard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class BoardControllerTest extends TestSupport {
    private final String URL = "/v1/board";

    @Test
    @DisplayName("게시글 등록에 성공한다.")
    void postBoardSuccess() throws Exception {
        //given
        RequestBoard requestBoard = RequestBoard.builder().title("테스트 글").content("테스트 내용").build();
        //when
        ResultActions perform = mock.perform(post(URL).header(AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertToString(requestBoard))
        ).andDo(print());
        //then
        assertTrue(perform.andReturn().getResponse().getContentAsString().contains("SUCCESS"));
    }
}