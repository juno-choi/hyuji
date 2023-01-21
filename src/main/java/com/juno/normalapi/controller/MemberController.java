package com.juno.normalapi.controller;

import com.juno.normalapi.api.Response;
import com.juno.normalapi.api.ResponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/member")
public class MemberController {

    @PostMapping("/login")
    public ResponseEntity<Response<String>> helloLogin(){
        return ResponseEntity.ok(Response.<String>builder()
                .code(ResponseCode.SUCCESS)
                .message("성공")
                .data("로그인 성공")
                .build());
    }
}
