package com.juno.normalapi.service.member;

import com.juno.normalapi.domain.dto.member.RequestJoinMember;
import com.juno.normalapi.domain.vo.member.JoinMember;
import com.juno.normalapi.domain.vo.member.LoginMember;

public interface MemberService {
    JoinMember join(RequestJoinMember requestJoinMember);

    LoginMember refresh(String token);
}
