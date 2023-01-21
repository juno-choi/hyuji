package com.juno.normalapi.service;

import com.juno.normalapi.domain.dto.RequestJoinMember;
import com.juno.normalapi.domain.vo.JoinMember;

public interface MemberService {
    JoinMember join(RequestJoinMember requestJoinMember);
}
