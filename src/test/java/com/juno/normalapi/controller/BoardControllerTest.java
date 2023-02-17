package com.juno.normalapi.controller;

import com.juno.normalapi.docs.TestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class BoardControllerTest extends TestSupport {
    private final String URL = "/v1/board";

    @Test
    @DisplayName("게시판 리스트 불러오기 성공")
    void getBoard() throws Exception {
        //given

        //when
        ResultActions perform = mock.perform(get(URL))
                .andDo(print());
        //then

    }
}