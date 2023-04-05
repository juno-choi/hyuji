package com.juno.normalapi.controller.member;

import com.juno.normalapi.docs.TestSupport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import java.nio.charset.StandardCharsets;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest extends TestSupport {

    private final String URL = "/v1/member";

    @Test
    @DisplayName("유효하지 않은 회원은 조회에 실패")
    void getMemberByIdFail1() throws Exception {
        // given
        // when
        ResultActions perform = mock.perform(get(URL + "/{member_id}", "0")
                .header(AUTHORIZATION, ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
        // then
        perform.andExpect(status().is4xxClientError());
        String contentAsString = perform.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertThat(contentAsString).contains("유효하지 않은 회원입니다.");
    }
}