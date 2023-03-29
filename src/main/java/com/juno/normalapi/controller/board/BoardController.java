package com.juno.normalapi.controller.board;

import com.juno.normalapi.annotation.V1;
import com.juno.normalapi.api.Response;
import com.juno.normalapi.api.ResponseCode;
import com.juno.normalapi.domain.dto.RequestBoard;
import com.juno.normalapi.domain.dto.RequestReply;
import com.juno.normalapi.domain.vo.BoardListVo;
import com.juno.normalapi.domain.vo.BoardVo;
import com.juno.normalapi.domain.vo.ReplyListVo;
import com.juno.normalapi.domain.vo.ReplyVo;
import com.juno.normalapi.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@V1
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping("")
    public ResponseEntity<Response<BoardVo>> postBoard(@RequestBody @Validated RequestBoard requestBoard, BindingResult bindingResult, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.<BoardVo>builder()
                .code(ResponseCode.SUCCESS)
                .message("성공")
                .data(boardService.postBoard(requestBoard, request))
                .build());
    }

    @GetMapping("")
    public ResponseEntity<Response<BoardListVo>> getBoardList(@PageableDefault(size = 5) Pageable pageable, HttpServletRequest request){
        return ResponseEntity.ok(Response.<BoardListVo>builder()
                .code(ResponseCode.SUCCESS)
                .message("성공")
                .data(boardService.getBoardList(pageable, request))
                .build());
    }

    @PostMapping("/reply")
    public ResponseEntity<Response<ReplyVo>> postReply(@RequestBody @Validated RequestReply requestReply, BindingResult bindingResult, HttpServletRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.<ReplyVo>builder()
                .code(ResponseCode.SUCCESS)
                .message("성공")
                .data(boardService.postReply(requestReply, request))
                .build());
    }

    @GetMapping("{board_id}")
    public ResponseEntity<Response<BoardVo>> getBoard(@PathVariable(name = "board_id") @NotNull(message = "게시판 id는 필수값 입니다.") Long boardId, HttpServletRequest request){
        return ResponseEntity.ok(Response.<BoardVo>builder()
                .code(ResponseCode.SUCCESS)
                .message("성공")
                .data(boardService.getBoard(boardId, request))
                .build());
    }

    @GetMapping("/reply")
    public ResponseEntity<Response<ReplyListVo>> getReplyList(
            @RequestParam(name = "board_id")
            @NotNull(message = "board_id는 필수값 입니다.")
            @Min(value = 1, message = "board_id 최소 값은 1입니다.") Long boardId,
            @PageableDefault(size = 5) Pageable pageable,
            HttpServletRequest request
    ){
        return ResponseEntity.ok(Response.<ReplyListVo>builder()
                .code(ResponseCode.SUCCESS)
                .message("성공")
                .data(boardService.getReplyList(boardId, pageable, request))
                .build());
    }
}
