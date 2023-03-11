package com.juno.normalapi.controller.board;

import com.juno.normalapi.annotation.V1;
import com.juno.normalapi.api.Response;
import com.juno.normalapi.api.ResponseCode;
import com.juno.normalapi.domain.dto.RequestBoard;
import com.juno.normalapi.domain.entity.Board;
import com.juno.normalapi.domain.vo.BoardListVo;
import com.juno.normalapi.domain.vo.BoardVo;
import com.juno.normalapi.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Response<BoardListVo>> board(@PageableDefault(size = 5) Pageable pageable, HttpServletRequest request){
        return ResponseEntity.ok(Response.<BoardListVo>builder()
                .code(ResponseCode.SUCCESS)
                .message("성공")
                .data(boardService.getBoardList(pageable, request))
                .build());
    }
}
