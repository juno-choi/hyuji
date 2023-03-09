package com.juno.normalapi.service;

import com.juno.normalapi.domain.dto.RequestBoard;
import com.juno.normalapi.domain.vo.BoardListVo;
import com.juno.normalapi.domain.vo.BoardVo;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

public interface BoardService {
    BoardListVo getBoardList();

    BoardVo postBoard(RequestBoard requestBoard, HttpServletRequest request);
}
