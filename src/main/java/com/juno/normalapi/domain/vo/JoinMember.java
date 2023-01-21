package com.juno.normalapi.domain.vo;

import com.juno.normalapi.domain.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class JoinMember {
    private Long memberId;
    private String email;
    private String name;
    private String nickname;
    private String tel;
    private String zipCode;
    private String address;
    private String addressDetail;
    private String role;

    public static JoinMember of(Member member){
        return JoinMember.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .tel(member.getTel())
                .zipCode(member.getZipCode())
                .address(member.getAddress())
                .addressDetail(member.getAddressDetail())
                .role(member.getRole())
                .build();
    }
}
