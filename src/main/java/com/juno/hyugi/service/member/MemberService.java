package com.juno.hyugi.service.member;

import com.juno.hyugi.domain.vo.member.MemberVo;

import javax.servlet.http.HttpServletRequest;

public interface MemberService {
    MemberVo getMemberById(Long memberId);
    MemberVo getMember(HttpServletRequest request);
}
