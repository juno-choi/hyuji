package com.juno.normalapi.service.board;

import com.juno.normalapi.domain.dto.RequestBoard;
import com.juno.normalapi.domain.entity.Board;
import com.juno.normalapi.domain.vo.BoardListVo;
import com.juno.normalapi.domain.vo.BoardVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

public interface BoardService {
    BoardVo postBoard(RequestBoard requestBoard, HttpServletRequest request);

    BoardListVo getBoardList(Pageable pageable, HttpServletRequest request);

    BoardVo getBoard(Long boardId, HttpServletRequest request);
}
