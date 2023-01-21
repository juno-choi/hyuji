package com.juno.normalapi.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class HelloControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private final String URL = "/hello";

    @Test
    @DisplayName("테스트1")
    void hello1() throws Exception {
        ResultActions perform = mockMvc.perform(get(URL + "/test"));
        perform.andDo(print());
    }

    @Test
    @DisplayName("로그인")
    void hello2() throws Exception {
        ResultActions perform = mockMvc.perform(get(URL + "/login"));
        perform.andDo(print());
    }

}