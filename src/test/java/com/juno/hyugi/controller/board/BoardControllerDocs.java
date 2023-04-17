package com.juno.hyugi.controller.board;

import com.juno.hyugi.config.DocsSupport;
import com.juno.hyugi.domain.dto.board.BoardDto;
import com.juno.hyugi.domain.dto.board.ReplyDto;
import com.juno.hyugi.domain.entity.board.Board;
import com.juno.hyugi.domain.entity.member.Member;
import com.juno.hyugi.domain.entity.board.Reply;
import com.juno.hyugi.repository.board.BoardRepository;
import com.juno.hyugi.repository.board.ReplyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class BoardControllerDocs extends DocsSupport {
    private final String URL = "/v1/board";

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    @DisplayName(URL + " (POST)")
    void postBoard() throws Exception {
        //given
        BoardDto boardDto = BoardDto.builder().title("테스트 글").content("테스트 내용").build();
        //when
        ResultActions perform = mock.perform(post(URL).header(AUTHORIZATION, ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertToString(boardDto))
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
                        fieldWithPath("data.created_at").type(JsonFieldType.STRING).description("등록일"),
                        fieldWithPath("data.modified_at").type(JsonFieldType.STRING).description("수정일")
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
                get(URL).param("page", "0").param("size", "5").header(AUTHORIZATION, ACCESS_TOKEN)
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
                        fieldWithPath("data.list[].created_at").type(JsonFieldType.STRING).description("등록일"),
                        fieldWithPath("data.list[].modified_at").type(JsonFieldType.STRING).description("수정일")
                )
        ));
    }

    @Test
    @DisplayName(URL + "/{board_id} (GET)")
    void getBoard() throws Exception {
        String testEmail = env.getProperty("normal.test.email");
        Member member = memberRepository.findByEmail(testEmail).get();
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
                RestDocumentationRequestBuilders.get(URL+"/{board_id}",saveBoard.getId()).header(AUTHORIZATION, ACCESS_TOKEN)
        );
        
        //then
        perform.andDo(docs.document(
                pathParameters(
                        parameterWithName("board_id").description("게시판 번호")
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
                        fieldWithPath("data.created_at").type(JsonFieldType.STRING).description("등록일"),
                        fieldWithPath("data.modified_at").type(JsonFieldType.STRING).description("수정일")
                )
        ));
    }

    @Test
    @DisplayName(URL+"/reply (POST)")
    void postReply() throws Exception {
        // given
        String testEmail = env.getProperty("normal.test.email");
        Member member = memberRepository.findByEmail(testEmail).get();

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

        ReplyDto replyDto = ReplyDto.builder()
                .boardId(saveBoard.getId())
                .content("댓글 달었지롱")
                .build();

        // when
        ResultActions perform = mock.perform(
                post(URL+"/reply").header(AUTHORIZATION, ACCESS_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertToString(replyDto))
        );

        // then
        perform.andDo(docs.document(
                requestFields(
                        fieldWithPath("board_id").type(JsonFieldType.NUMBER).description("게시글 id"),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용")
                ),
                responseFields(
                        fieldWithPath("code").type(JsonFieldType.STRING).description("결과 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("댓글 id"),
                        fieldWithPath("data.board_id").type(JsonFieldType.NUMBER).description("게시판 id"),
                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("댓글 내용"),
                        fieldWithPath("data.created_at").type(JsonFieldType.STRING).description("작성일"),
                        fieldWithPath("data.modified_at").type(JsonFieldType.STRING).description("수정일"),
                        fieldWithPath("data.writer").type(JsonFieldType.STRING).description("작성자")
                )
        ));
    }

    @Test
    @DisplayName("/reply (GET)")
    void getReplyList() throws Exception {
        // given
        String testEmail = env.getProperty("normal.test.email");
        Member member = memberRepository.findByEmail(testEmail).get();
        Board saveBoard = boardRepository.save(Board.of(member, "댓글 달려", "댓글이 달려요"));
        // 댓글 100개 달기
        for(int i=0; i<100; i++){
            replyRepository.save(Reply.of(member, saveBoard, "댓글"+i));
        }

        // when
        ResultActions perform = mock.perform(
                get(URL + "/reply").header(AUTHORIZATION, ACCESS_TOKEN)
                .param("board_id", saveBoard.getId().toString())
                .param("page", "1")
                .param("size", "10")
        );

        // then
        perform.andDo(docs.document(
                requestParameters(
                        parameterWithName("board_id").description("게시판 id"),
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
                        fieldWithPath("data.list[].id").type(JsonFieldType.NUMBER).description("댓글 id"),
                        fieldWithPath("data.list[].board_id").type(JsonFieldType.NUMBER).description("게시판 id"),
                        fieldWithPath("data.list[].content").type(JsonFieldType.STRING).description("내용"),
                        fieldWithPath("data.list[].writer").type(JsonFieldType.STRING).description("닉네임"),
                        fieldWithPath("data.list[].created_at").type(JsonFieldType.STRING).description("등록일"),
                        fieldWithPath("data.list[].modified_at").type(JsonFieldType.STRING).description("수정일")
                )
        ));

    }
}