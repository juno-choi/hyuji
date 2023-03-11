package com.juno.normalapi.service.board;

import com.juno.normalapi.domain.dto.RequestBoard;
import com.juno.normalapi.domain.entity.Board;
import com.juno.normalapi.domain.vo.BoardVo;
import com.juno.normalapi.repository.board.BoardRepository;
import com.juno.normalapi.service.ServiceTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardServiceImplTest extends ServiceTestSupport {
    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    private MockHttpServletRequest request = new MockHttpServletRequest();

    @Test
    @DisplayName("게시글 등록에 성공한다.")
    void postBoardSuccess() {
        // given
        RequestBoard requestBoard = RequestBoard.builder()
                .title("제목")
                .content("내용")
                .build();

        request.setAttribute("loginMemberId", member.getMemberId());

        // when
        BoardVo saveBoard = boardService.postBoard(requestBoard, request);

        // then
        Long boardId = saveBoard.getBoardId();
        assertNotNull(boardRepository.findById(boardId));
    }

    @Test
    @DisplayName("게시글 불러오기에 성공한다.")
    void getBoardSuccess() {
        //given
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

        Pageable pageable = Pageable.ofSize(5);
        pageable = pageable.next();
        request.setAttribute("loginMemberId", member.getMemberId());

        //when
        Page<Board> boardList = boardService.getBoardList(pageable, request);

        //then
        assertNotNull(boardList);
    }
}