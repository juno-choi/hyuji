package com.juno.hyugi.service.board;

import com.juno.hyugi.domain.dto.board.BoardDto;
import com.juno.hyugi.domain.dto.board.ReplyDto;
import com.juno.hyugi.domain.vo.board.BoardListVo;
import com.juno.hyugi.domain.vo.board.BoardVo;
import com.juno.hyugi.domain.vo.board.ReplyListVo;
import com.juno.hyugi.domain.vo.board.ReplyVo;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;

public interface BoardService {
    BoardVo postBoard(BoardDto boardDto, HttpServletRequest request);

    BoardListVo getBoardList(Pageable pageable, HttpServletRequest request);

    BoardVo getBoard(Long boardId, HttpServletRequest request);

    ReplyVo postReply(ReplyDto replyDto, HttpServletRequest request);

    ReplyListVo getReplyList(Long boardId, Pageable pageable, HttpServletRequest request);
}
