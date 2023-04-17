package com.juno.hyugi.service.member;

import com.juno.hyugi.domain.entity.member.Member;
import com.juno.hyugi.domain.vo.member.MemberVo;
import com.juno.hyugi.exception.UnauthorizedException;
import com.juno.hyugi.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final Environment env;

    @Override
    public MemberVo getMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new IllegalArgumentException("유효하지 않은 회원입니다.")
        );
        return MemberVo.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .tel(member.getTel())
                .joinType(member.getType())
                .zipCode(member.getZipCode())
                .address(member.getAddress())
                .addressDetail(member.getAddressDetail())
                .createdAt(member.getCreatedAt())
                .modifiedAt(member.getModifiedAt())
                .build();
    }

    @Override
    public MemberVo getMember(HttpServletRequest request) {
        Long loginUserId = Long.valueOf(request.getAttribute(env.getProperty("normal.login.attribute")).toString());

        Member member = memberRepository.findById(loginUserId).orElseThrow(() -> new UnauthorizedException("잘못된 접근입니다."));
        return MemberVo.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .tel(member.getTel())
                .joinType(member.getType())
                .zipCode(member.getZipCode())
                .address(member.getAddress())
                .addressDetail(member.getAddressDetail())
                .createdAt(member.getCreatedAt())
                .modifiedAt(member.getModifiedAt())
                .build();
    }
}
