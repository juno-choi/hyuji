package com.juno.normalapi.service.board;

import com.juno.normalapi.domain.dto.RequestBoard;
import com.juno.normalapi.exception.UnauthorizedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardServiceImplTest {
    @Autowired
    private BoardService boardService;

    @Autowired
    private Environment env;

    private MockHttpServletRequest request = new MockHttpServletRequest();

    @Test
    @DisplayName("게시글 등록에 성공한다.")
    void postBoardFail2() {
        // given
        RequestBoard requestBoard = RequestBoard.builder()
                .title("제목")
                .content("내용")
                .build();

        request.setAttribute("loginMemberId", 1L);
        // when
        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () -> boardService.postBoard(requestBoard, request));

        // then
        assertEquals("잘못된 접근입니다.", ex.getMessage());
    }


}