package com.juno.hyugi.controller.rest;

import com.juno.hyugi.config.TestSupport;
import com.juno.hyugi.domain.entity.rest.RestAreaInfo;
import com.juno.hyugi.repository.rest.RestAreaInfoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class RestAreaControllerTest extends TestSupport {
    private final String URL = "/rest";

    @Autowired
    private RestAreaInfoRepository restAreaInfoRepository;

    @Test
    @DisplayName("휴게소 조회에 성공")
    void restAreaSearch() throws Exception {
        String restAsString1 = "성남 휴게소(판교 방향)";
        String restAsString2 = "성남 휴게소(인천 방향)";
        //given
        restAreaInfoRepository.save(
                RestAreaInfo.builder()
                        .svarNm(restAsString1)
                        .build()
        );

        restAreaInfoRepository.save(
                RestAreaInfo.builder()
                        .svarNm(restAsString2)
                        .build()
        );

        List<RestAreaInfo> all = restAreaInfoRepository.findAll();
        //when
        ResultActions perform = mock.perform(get(URL+"/search").param("keyword", "성남"))
                .andDo(print());
        //then
        String contentAsString = perform.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertThat(contentAsString).contains(restAsString1);
        assertThat(contentAsString).contains(restAsString2);
    }
}