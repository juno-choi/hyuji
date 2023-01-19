package com.juno.normalapi.controller;

import com.juno.normalapi.api.Response;
import com.juno.normalapi.api.ResponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @GetMapping("/{user}")
    public ResponseEntity<Response<String>> hello(@PathVariable(name = "user") String user){
        return ResponseEntity.ok(Response.<String>builder()
                .code(ResponseCode.SUCCESS)
                .message("성공")
                .data("hello "+user)
                .build());
    }
}