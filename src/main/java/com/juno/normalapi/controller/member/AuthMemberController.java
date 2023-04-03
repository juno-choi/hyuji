package com.juno.normalapi.controller.member;

import com.juno.normalapi.api.Response;
import com.juno.normalapi.api.ResponseCode;
import com.juno.normalapi.domain.dto.member.JoinMemberDto;
import com.juno.normalapi.domain.vo.member.JoinMemberVo;
import com.juno.normalapi.domain.vo.member.LoginMemberVo;
import com.juno.normalapi.domain.vo.member.MemberVo;
import com.juno.normalapi.service.member.AuthMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/member")
@RequiredArgsConstructor
public class AuthMemberController {
    private final AuthMemberService authMemberService;

    @PostMapping("/join")
    public ResponseEntity<Response<JoinMemberVo>> join(@RequestBody JoinMemberDto joinMember){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.<JoinMemberVo>builder()
                .code(ResponseCode.SUCCESS)
                .message("회원 가입 성공")
                .data(authMemberService.join(joinMember))
                .build());
    }

    @GetMapping("/refresh/{token}")
    public ResponseEntity<Response<LoginMemberVo>> refresh(@PathVariable(value = "token") String token){
        return ResponseEntity.ok(Response.<LoginMemberVo>builder()
                .code(ResponseCode.SUCCESS)
                .message("토큰 재발급 성공")
                .data(authMemberService.refresh(token))
                .build());
    }
}
