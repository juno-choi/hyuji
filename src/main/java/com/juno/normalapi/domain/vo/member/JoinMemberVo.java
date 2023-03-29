package com.juno.normalapi.domain.vo.member;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.juno.normalapi.domain.entity.member.Member;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JoinMemberVo {
    private Long memberId;
    private String email;
    private String name;
    private String nickname;
    private String tel;
    private String zipCode;
    private String address;
    private String addressDetail;
    private String role;

    public static JoinMemberVo of(Member member){
        return JoinMemberVo.builder()
                .memberId(member.getId())
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
