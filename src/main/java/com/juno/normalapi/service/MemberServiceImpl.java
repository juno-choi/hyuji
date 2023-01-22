package com.juno.normalapi.service;

import com.juno.normalapi.domain.dto.RequestJoinMember;
import com.juno.normalapi.domain.entity.Member;
import com.juno.normalapi.domain.enums.JoinType;
import com.juno.normalapi.domain.vo.JoinMember;
import com.juno.normalapi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public JoinMember join(RequestJoinMember requestJoinMember) {
        Member member = Member.of(requestJoinMember, JoinType.EMAIL);
        member.encryptPassword(member, passwordEncoder);
        Member saveMember = memberRepository.save(member);
        return JoinMember.of(saveMember);
    }
}
