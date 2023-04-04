package com.juno.normalapi.service.member;

import com.juno.normalapi.domain.vo.member.MemberVo;

import javax.servlet.http.HttpServletRequest;

public interface MemberService {
    MemberVo getMemberById(Long memberId);
    MemberVo getMember(HttpServletRequest request);
}
