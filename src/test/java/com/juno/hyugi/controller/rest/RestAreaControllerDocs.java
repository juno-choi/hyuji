package com.juno.hyugi.controller.rest;

import com.juno.hyugi.config.DocsSupport;
import com.juno.hyugi.domain.entity.rest.RestAreaInfo;
import com.juno.hyugi.repository.rest.RestAreaInfoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;


import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class RestAreaControllerDocs extends DocsSupport {
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
                        .bsopAdtnlFcltCd("")
                        .cocrPrkgTrcn("")
                        .dspnPrkgTrcn("")
                        .fscarPrkgTrcn("")
                        .gudClssCd("")
                        .gudClssNm("")
                        .hdqrCd("")
                        .hdqrNm("")
                        .mtnofCd("")
                        .mtnofNm("")
                        .pstnoCd("")
                        .routeCd("")
                        .routeNm("")
                        .rprsTelNo("")
                        .svarAddr("")
                        .svarCd("")
                        .svarGsstClssCd("")
                        .svarGsstClssNm("")
                        .build()
        );

        restAreaInfoRepository.save(
                RestAreaInfo.builder()
                        .svarNm(restAsString2)
                        .bsopAdtnlFcltCd("")
                        .cocrPrkgTrcn("")
                        .dspnPrkgTrcn("")
                        .fscarPrkgTrcn("")
                        .gudClssCd("")
                        .gudClssNm("")
                        .hdqrCd("")
                        .hdqrNm("")
                        .mtnofCd("")
                        .mtnofNm("")
                        .pstnoCd("")
                        .routeCd("")
                        .routeNm("")
                        .rprsTelNo("")
                        .svarAddr("")
                        .svarCd("")
                        .svarGsstClssCd("")
                        .svarGsstClssNm("")
                        .build()
        );

        //when
        ResultActions perform = mock.perform(get(URL+"/search").param("keyword", "성남"))
                .andDo(print());

        //then
        perform.andDo(docs.document(
                requestParameters(
                        parameterWithName("keyword").description("조회할 휴게소 이름")
                ),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                        fieldWithPath("data.list[].id").type(JsonFieldType.NUMBER).description("id"),
                        fieldWithPath("data.list[].routeCd").type(JsonFieldType.STRING).description("휴게소 정보"),
                        fieldWithPath("data.list[].svarAddr").type(JsonFieldType.STRING).description(""),
                        fieldWithPath("data.list[].routeNm").type(JsonFieldType.STRING).description(""),
                        fieldWithPath("data.list[].hdqrNm").type(JsonFieldType.STRING).description(""),
                        fieldWithPath("data.list[].mtnofNm").type(JsonFieldType.STRING).description(""),
                        fieldWithPath("data.list[].svarCd").type(JsonFieldType.STRING).description(""),
                        fieldWithPath("data.list[].svarNm").type(JsonFieldType.STRING).description(""),
                        fieldWithPath("data.list[].hdqrCd").type(JsonFieldType.STRING).description(""),
                        fieldWithPath("data.list[].mtnofCd").type(JsonFieldType.STRING).description(""),
                        fieldWithPath("data.list[].svarGsstClssCd").type(JsonFieldType.STRING).description(""),
                        fieldWithPath("data.list[].svarGsstClssNm").type(JsonFieldType.STRING).description(""),
                        fieldWithPath("data.list[].gudClssCd").type(JsonFieldType.STRING).description(""),
                        fieldWithPath("data.list[].gudClssNm").type(JsonFieldType.STRING).description(""),
                        fieldWithPath("data.list[].pstnoCd").type(JsonFieldType.STRING).description(""),
                        fieldWithPath("data.list[].cocrPrkgTrcn").type(JsonFieldType.STRING).description(""),
                        fieldWithPath("data.list[].fscarPrkgTrcn").type(JsonFieldType.STRING).description(""),
                        fieldWithPath("data.list[].dspnPrkgTrcn").type(JsonFieldType.STRING).description(""),
                        fieldWithPath("data.list[].bsopAdtnlFcltCd").type(JsonFieldType.STRING).description(""),
                        fieldWithPath("data.list[].rprsTelNo").type(JsonFieldType.STRING).description("")

                )
            )
        );
    }
}