package com.juno.normalapi.controller;

import com.juno.normalapi.api.Response;
import com.juno.normalapi.api.ResponseCode;
import com.juno.normalapi.domain.dto.RequestJoinMember;
import com.juno.normalapi.domain.vo.JoinMember;
import com.juno.normalapi.domain.vo.LoginMember;
import com.juno.normalapi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<Response<JoinMember>> join(@RequestBody RequestJoinMember joinMember){
        return ResponseEntity.ok(Response.<JoinMember>builder()
                .code(ResponseCode.SUCCESS)
                .message("회원 가입 성공")
                .data(memberService.join(joinMember))
                .build());
    }

    @GetMapping("/refresh/{token}")
    public ResponseEntity<Response<LoginMember>> refresh(@PathVariable(value = "token") String token){
        return ResponseEntity.ok(Response.<LoginMember>builder()
                .code(ResponseCode.SUCCESS)
                .message("회원 가입 성공")
                .data(memberService.refresh(token))
                .build());
    }
}
