package com.juno.normalapi.service.board;

import com.juno.normalapi.domain.dto.board.RequestBoard;
import com.juno.normalapi.domain.dto.board.RequestReply;
import com.juno.normalapi.domain.vo.board.BoardListVo;
import com.juno.normalapi.domain.vo.board.BoardVo;
import com.juno.normalapi.domain.vo.board.ReplyListVo;
import com.juno.normalapi.domain.vo.board.ReplyVo;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;

public interface BoardService {
    BoardVo postBoard(RequestBoard requestBoard, HttpServletRequest request);

    BoardListVo getBoardList(Pageable pageable, HttpServletRequest request);

    BoardVo getBoard(Long boardId, HttpServletRequest request);

    ReplyVo postReply(RequestReply requestReply, HttpServletRequest request);

    ReplyListVo getReplyList(Long boardId, Pageable pageable, HttpServletRequest request);
}
