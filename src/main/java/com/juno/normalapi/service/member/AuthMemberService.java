package com.juno.normalapi.service.member;

import com.juno.normalapi.domain.dto.member.JoinMemberDto;
import com.juno.normalapi.domain.vo.member.JoinMemberVo;
import com.juno.normalapi.domain.vo.member.LoginMemberVo;
import com.juno.normalapi.domain.vo.member.MemberVo;

public interface AuthMemberService {
    JoinMemberVo join(JoinMemberDto joinMemberDto);

    LoginMemberVo refresh(String token);

    MemberVo getMember(Long memberId);
}
