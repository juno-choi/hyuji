package com.juno.normalapi.service;

import com.juno.normalapi.domain.dto.RequestBoard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.naming.AuthenticationException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardServiceImplTest {
    @Autowired
    private BoardService boardService;

    @Autowired
    private Environment env;

    private MockHttpServletRequest request = new MockHttpServletRequest();

    @Test
    @DisplayName("게시글 등록에 실패한다.")
    void postBoardFail1() throws AuthenticationException {
        // given
        RequestBoard requestBoard = RequestBoard.builder()
                .title("제목")
                .content("내용")
                .build();

        request.setAttribute("loginMemberId", 1L);
        System.out.println(request.getAttribute("loginMemberId").toString());
        // when
        boardService.postBoard(requestBoard, request);
        // then
    }
}