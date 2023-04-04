package com.juno.normalapi.controller.member;

import com.juno.normalapi.annotation.V1;
import com.juno.normalapi.api.Response;
import com.juno.normalapi.api.ResponseCode;
import com.juno.normalapi.domain.vo.member.MemberVo;
import com.juno.normalapi.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@V1
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/{member_id}")
    public ResponseEntity<Response<MemberVo>> getMemberById(@PathVariable(name = "member_id") Long memberId){
        return ResponseEntity.ok(Response.<MemberVo>builder()
                .code(ResponseCode.SUCCESS)
                .message("성공")
                .data(memberService.getMemberById(memberId))
                .build());
    }

    @GetMapping("")
    public ResponseEntity<Response<MemberVo>> getMember(HttpServletRequest request){
        return ResponseEntity.ok(Response.<MemberVo>builder()
                .code(ResponseCode.SUCCESS)
                .message("성공")
                .data(memberService.getMember(request))
                .build());
    }

}
