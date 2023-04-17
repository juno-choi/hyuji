package com.juno.hyugi.service.member;

import com.juno.hyugi.domain.dto.member.JoinMemberDto;
import com.juno.hyugi.domain.vo.member.JoinMemberVo;
import com.juno.hyugi.domain.vo.member.LoginMemberVo;

public interface AuthMemberService {
    JoinMemberVo join(JoinMemberDto joinMemberDto);

    LoginMemberVo refresh(String token);


}
