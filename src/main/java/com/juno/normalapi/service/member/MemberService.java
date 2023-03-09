package com.juno.normalapi.service.member;

import com.juno.normalapi.domain.dto.RequestJoinMember;
import com.juno.normalapi.domain.vo.JoinMember;
import com.juno.normalapi.domain.vo.LoginMember;

public interface MemberService {
    JoinMember join(RequestJoinMember requestJoinMember);

    LoginMember refresh(String token);
}
