package com.juno.normalapi.controller;

import com.juno.normalapi.annotation.V1;
import com.juno.normalapi.api.Response;
import com.juno.normalapi.api.ResponseCode;
import com.juno.normalapi.domain.dto.RequestBoard;
import com.juno.normalapi.domain.vo.BoardListVo;
import com.juno.normalapi.domain.vo.BoardVo;
import com.juno.normalapi.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@V1
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping("")
    public ResponseEntity<Response<BoardVo>> postBoard(@RequestBody RequestBoard requestBoard, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.<BoardVo>builder()
                .code(ResponseCode.SUCCESS)
                .message("성공")
                .data(boardService.postBoard(requestBoard, request))
                .build());
    }

    @GetMapping("")
    public ResponseEntity<Response<BoardListVo>> board(HttpServletRequest request){
        return ResponseEntity.ok(Response.<BoardListVo>builder()
                .code(ResponseCode.SUCCESS)
                .message("성공")
                .data(boardService.getBoardList())
                .build());
    }
}
