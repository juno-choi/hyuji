package com.juno.normalapi.controller.board;

import com.juno.normalapi.docs.TestSupport;
import com.juno.normalapi.domain.dto.board.BoardDto;
import com.juno.normalapi.domain.dto.board.ReplyDto;
import com.juno.normalapi.domain.entity.board.Board;
import com.juno.normalapi.domain.entity.member.Member;
import com.juno.normalapi.repository.board.BoardRepository;
import com.juno.normalapi.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BoardControllerTest extends TestSupport {
    private final String URL = "/v1/board";

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private Environment env;

    @Test
    @DisplayName("게시글 등록에 성공")
    void postBoardSuccess() throws Exception {
        //given
        BoardDto boardDto = BoardDto.builder().title("테스트 글").content("테스트 내용").build();
        //when
        ResultActions perform = mock.perform(post(URL).header(AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertToString(boardDto))
        ).andDo(print());
        //then
        assertTrue(perform.andReturn().getResponse().getContentAsString().contains("SUCCESS"));
    }

    @Test
    @DisplayName("게시글 리스트 불러오기에 성공")
    void getBoardListSuccess() throws Exception {
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
                get(URL + "?page=0&size=5").header(AUTHORIZATION, accessToken)
        ).andDo(print());
        //then
        perform.andReturn().getResponse().getContentAsString().contains("SUCCESS");
    }

    @Test
    @DisplayName("board id 값이 비어있어 댓글 등록에 실패")
    void postReplyFail1() throws Exception {
        // given
        // when
        ResultActions perform = mock.perform(
                post(URL + "/reply")
                        .header(AUTHORIZATION, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToString(ReplyDto.builder().build()))
        ).andDo(print());

        // then
        assertTrue(
                perform.andReturn().getResponse()
                        .getContentAsString(StandardCharsets.UTF_8)
                        .contains("board_id는 필수 값입니다.")
        );
    }

    @Test
    @DisplayName("board id 유효하지 않아 게시글 상세 불러오기에 실패")
    void getBoardFail1() throws Exception {
        // given
        // when
        ResultActions perform = mock.perform(
                get(URL + "/{boardId}", 0L)
                        .header(AUTHORIZATION, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());
        // then
        assertTrue(
                perform.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8)
                        .contains("유효하지 않은 게시판 번호입니다.")
        );
    }

    @Test
    @DisplayName("댓글 페이징 board_id 값이 빠져 불러오는데 실패")
    void getReplyListFail1() throws Exception {
        // given
        // when
        ResultActions perform = mock.perform(
                get(URL + "/reply").header(AUTHORIZATION, accessToken)
                        .param("page", "1")
                        .param("size", "10")
        ).andDo(print());
        // then
        perform.andExpect(status().is4xxClientError());
        assertTrue(
                perform.andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8)
                        .contains("board_id (Long) 값이 비어있습니다.")
        );
    }
}