package com.juno.normalapi.domain.entity;

import com.juno.normalapi.domain.dto.RequestJoinMember;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String email;
    private String password;
    private String name;
    private String nickname;
    private String tel;
    private String zipCode;
    private String address;
    private String addressDetail;
    private String role;

    // 일반 유저
    public static Member of(RequestJoinMember requestJoinMember){
        return Member.builder()
                .email(requestJoinMember.getEmail())
                .password(requestJoinMember.getPassword())
                .name(requestJoinMember.getName())
                .nickname(requestJoinMember.getNickname())
                .tel(requestJoinMember.getTel())
                .zipCode(requestJoinMember.getZipCode())
                .address(requestJoinMember.getAddress())
                .addressDetail(requestJoinMember.getAddressDetail())
                .role("ROLE_MEMBER")
                .build();
    }

    public void encryptPassword(Member member, BCryptPasswordEncoder encoder){
        this.password = encoder.encode(member.getPassword());
    }

    public void changeRole(String role){
        this.role = role;
    }
}
