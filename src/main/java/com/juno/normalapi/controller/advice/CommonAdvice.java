package com.juno.normalapi.controller.advice;

import com.juno.normalapi.api.ErrorDto;
import com.juno.normalapi.api.ResponseCode;
import com.juno.normalapi.api.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice(basePackages = "com.juno.normalapi")
public class CommonAdvice {
    @ExceptionHandler
    public ResponseEntity<ResponseError<ErrorDto>> illegalArgumentException(IllegalArgumentException e){
        List<ErrorDto> errors = new ArrayList<>();
        errors.add(ErrorDto.builder().error(e.getMessage()).build());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ResponseError.<ErrorDto>builder()
                                .code(ResponseCode.FAIL)
                                .message("잘못된 입력입니다.")
                                .errors(errors)
                                .build()
                );
    }
}
