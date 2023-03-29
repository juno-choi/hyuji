package com.juno.normalapi.service.board;

import com.juno.normalapi.domain.dto.board.BoardDto;
import com.juno.normalapi.domain.dto.board.ReplyDto;
import com.juno.normalapi.domain.entity.board.Board;
import com.juno.normalapi.domain.entity.board.Reply;
import com.juno.normalapi.domain.vo.board.BoardListVo;
import com.juno.normalapi.domain.vo.board.BoardVo;
import com.juno.normalapi.domain.vo.board.ReplyListVo;
import com.juno.normalapi.domain.vo.board.ReplyVo;
import com.juno.normalapi.repository.board.BoardRepository;
import com.juno.normalapi.repository.board.ReplyRepository;
import com.juno.normalapi.service.ServiceTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Autowired
    private ReplyRepository replyRepository;

    private MockHttpServletRequest request = new MockHttpServletRequest();

    @Test
    @DisplayName("게시글 등록에 성공한다.")
    void postBoardSuccess() {
        // given
        BoardDto boardDto = BoardDto.builder()
                .title("제목")
                .content("내용")
                .build();

        request.setAttribute("loginMemberId", member.getMemberId());

        // when
        BoardVo saveBoard = boardService.postBoard(boardDto, request);

        // then
        Long boardId = saveBoard.getBoardId();
        assertNotNull(boardRepository.findById(boardId));
    }

    @Test
    @DisplayName("게시글 리스트 불러오기에 성공한다.")
    void getBoardListSuccess() {
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
        BoardListVo boardList = boardService.getBoardList(pageable, request);

        //then
        assertNotNull(boardList);
    }

    @Test
    @DisplayName("유효하지 않은 게시물은 불러오는데 실패한다.")
    void getBoardFail1() throws Exception {
        //given
        //when
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> boardService.getBoard(0L, request));

        //then
        assertEquals("유효하지 않은 게시판 번호입니다.", ex.getMessage());
    }

    @Test
    @DisplayName("게시물 불러오는데 성공한다.")
    void getBoardSuccess1() throws Exception {
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
        replyRepository.save(Reply.of(member, saveBoard, "댓글 달아보자"));
        replyRepository.save(Reply.of(member, saveBoard, "댓글 달아보자2"));

        //when
        BoardVo board = boardService.getBoard(saveBoard.getId(), request);

        //then
        assertNotNull(board);
    }

    @Test
    @DisplayName("게시판 댓글 등록에 성공한다.")
    void postReplyFail1(){
        // given
        Board saveBoard = boardRepository.save(Board.of(member, "댓글 달려", "댓글이 달려요"));
        String content = "댓글이 달림";

        ReplyDto replyDto = ReplyDto.builder()
                .boardId(saveBoard.getId())
                .content(content)
                .build();

        request.setAttribute("loginMemberId", member.getMemberId());

        // when
        ReplyVo replyVo = boardService.postReply(replyDto, request);

        // then
        assertEquals(content, replyVo.getContent());
    }

    @Test
    @DisplayName("댓글을 페이징하여 불러오는데 성공한다.")
    void getReplyListSuccess1(){
        // given
        Board saveBoard = boardRepository.save(Board.of(member, "댓글 달려", "댓글이 달려요"));
        // 댓글 100개 달기
        for(int i=0; i<100; i++){
            replyRepository.save(Reply.of(member, saveBoard, "댓글"+i));
        }

        Pageable pageable = Pageable.ofSize(5);
        pageable = pageable.next();
        // when
        ReplyListVo replyList = boardService.getReplyList(saveBoard.getId(), pageable, request);
        // then
        assertTrue(!replyList.getEmpty());
    }
}