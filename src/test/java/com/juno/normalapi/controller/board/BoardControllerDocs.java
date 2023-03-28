package com.juno.normalapi.controller.board;

import com.juno.normalapi.docs.DocsSupport;
import com.juno.normalapi.domain.dto.RequestBoard;
import com.juno.normalapi.domain.entity.Board;
import com.juno.normalapi.domain.entity.Member;
import com.juno.normalapi.repository.board.BoardRepository;
import com.juno.normalapi.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class BoardControllerDocs extends DocsSupport {
    private final String URL = "/v1/board";

    @Autowired
    private Environment env;

    @Autowired
    private BoardRepository boardRepository;

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
                        fieldWithPath("data.reply_count").type(JsonFieldType.NUMBER).description("댓글수"),
                        fieldWithPath("data.created_at").type(JsonFieldType.STRING).description("등록일")
                )
        ));
    }

    @Test
    @DisplayName(URL + " (GET)")
    void getBoardList() throws Exception {
        //given
        Member member = memberRepository.findByEmail(env.getProperty("normal.test.email")).get();
        for(int i=0; i<20; i++){
            LocalDateTime now = LocalDateTime.now();
            boardRepository.save(
                    Board.builder()
                            .title("테스트 "+i)
                            .content("내용 "+i)
                            .member(member)
                            .createdAt(now)
                            .modifiedAt(now)
                            .build()
            );
        }
        //when
        ResultActions perform = mock.perform(
                get(URL).param("page", "0").param("size", "5").header(AUTHORIZATION, accessToken)
        );
        //then
        perform.andDo(docs.document(
                requestParameters(
                        parameterWithName("page").description("페이지 번호 (default = 0)").optional(),
                        parameterWithName("size").description("페이지 당 호출할 개수 (default = 5)").optional()
                ),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                        fieldWithPath("data.total_page").type(JsonFieldType.NUMBER).description("총 페이지 수"),
                        fieldWithPath("data.total_elements").type(JsonFieldType.NUMBER).description("총 게시글 수"),
                        fieldWithPath("data.number_of_elements").type(JsonFieldType.NUMBER).description("호출한 페이지 게시글 수"),
                        fieldWithPath("data.last").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                        fieldWithPath("data.empty").type(JsonFieldType.BOOLEAN).description("호출 페이지 빈 페이지 여부"),
                        fieldWithPath("data.list[].board_id").type(JsonFieldType.NUMBER).description("게시글 번호"),
                        fieldWithPath("data.list[].member_id").type(JsonFieldType.NUMBER).description("회원 번호"),
                        fieldWithPath("data.list[].title").type(JsonFieldType.STRING).description("제목"),
                        fieldWithPath("data.list[].content").type(JsonFieldType.STRING).description("내용"),
                        fieldWithPath("data.list[].writer").type(JsonFieldType.STRING).description("닉네임"),
                        fieldWithPath("data.list[].reply_count").type(JsonFieldType.NUMBER).description("댓글수"),
                        fieldWithPath("data.list[].created_at").type(JsonFieldType.STRING).description("등록일")
                )
        ));
    }

    @Test
    @DisplayName(URL + "/{board_id} (GET)")
    void getBoard() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now();
        Board saveBoard = boardRepository.save(
                Board.builder()
                        .title("테스트")
                        .content("내용")
                        .member(member)
                        .createdAt(now)
                        .modifiedAt(now)
                        .build()
        );

        //when
        ResultActions perform = mock.perform(
                get(URL+"/"+saveBoard.getId()).header(AUTHORIZATION, accessToken)
        );

        //then
        perform.andDo(docs.document(
                requestParameters(
                        parameterWithName("page").description("페이지 번호 (default = 0)").optional(),
                        parameterWithName("size").description("페이지 당 호출할 개수 (default = 5)").optional()
                ),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                        fieldWithPath("data.board_id").type(JsonFieldType.NUMBER).description("게시글 번호"),
                        fieldWithPath("data.member_id").type(JsonFieldType.NUMBER).description("회원 번호"),
                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("제목"),
                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("내용"),
                        fieldWithPath("data.writer").type(JsonFieldType.STRING).description("닉네임"),
                        fieldWithPath("data.reply_count").type(JsonFieldType.NUMBER).description("댓글수"),
                        fieldWithPath("data.created_at").type(JsonFieldType.STRING).description("등록일")
                )
        ));
    }
}