package com.juno.hyugi.exception.advice;

import com.juno.hyugi.api.ResponseCode;
import com.juno.hyugi.api.ResponseError;
import com.juno.hyugi.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice(basePackages = "com.juno.normalapi")
public class CommonAdvice {
    @ExceptionHandler
    public ResponseEntity<ResponseError<String>> illegalArgumentException(IllegalArgumentException e){
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ResponseError.<String>builder()
                                .code(ResponseCode.FAIL)
                                .message("잘못된 입력입니다.")
                                .errors(errors)
                                .build()
                );
    }

    @ExceptionHandler
    public ResponseEntity<ResponseError<String>> authenticationException(UnauthorizedException e){
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        ResponseError.<String>builder()
                                .code(ResponseCode.FAIL)
                                .message("인증되지 않은 접근입니다.")
                                .errors(errors)
                                .build()
                );
    }

    @ExceptionHandler
    public ResponseEntity<ResponseError<String>> missingServletRequestParameterException(MissingServletRequestParameterException e){
        List<String> errors = new ArrayList<>();
        errors.add(String.format("%s (%s) 값이 비어있습니다.", e.getParameterName(), e.getParameterType()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ResponseError.<String>builder()
                                .code(ResponseCode.FAIL)
                                .message("파라미터를 확인해주세요.")
                                .errors(errors)
                                .build()
                );
    }
}
