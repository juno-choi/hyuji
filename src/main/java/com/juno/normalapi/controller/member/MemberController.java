package com.juno.normalapi.controller.member;

import com.juno.normalapi.annotation.V1;
import com.juno.normalapi.api.Response;
import com.juno.normalapi.api.ResponseCode;
import com.juno.normalapi.domain.vo.member.MemberVo;
import com.juno.normalapi.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@V1
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/{member_id}")
    public ResponseEntity<Response<MemberVo>> getMember(@PathVariable(name = "member_id") Long memberId){
        return ResponseEntity.ok(Response.<MemberVo>builder()
                .code(ResponseCode.SUCCESS)
                .message("성공")
                .data(memberService.getMember(memberId))
                .build());
    }
}
