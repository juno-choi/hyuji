package com.juno.normalapi.service.member;

import com.juno.normalapi.domain.vo.member.MemberVo;

public interface MemberService {
    MemberVo getMember(Long memberId);
}
