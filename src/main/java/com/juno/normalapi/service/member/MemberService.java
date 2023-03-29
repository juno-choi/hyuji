package com.juno.normalapi.service.member;

import com.juno.normalapi.domain.dto.member.JoinMemberDto;
import com.juno.normalapi.domain.vo.member.JoinMemberVo;
import com.juno.normalapi.domain.vo.member.LoginMemberVo;

public interface MemberService {
    JoinMemberVo join(JoinMemberDto joinMemberDto);

    LoginMemberVo refresh(String token);
}
