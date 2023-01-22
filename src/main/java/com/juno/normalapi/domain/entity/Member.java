package com.juno.normalapi.domain.entity;

import com.juno.normalapi.domain.dto.RequestJoinMember;
import com.juno.normalapi.domain.enums.JoinType;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;

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

    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String nickname;
    @Column(nullable = false)
    private String tel;

    private JoinType type;
    private String zipCode;
    private String address;
    private String addressDetail;
    private String role;

    // 일반 유저
    public static Member of(RequestJoinMember requestJoinMember, JoinType joinType){
        return Member.builder()
                .email(requestJoinMember.getEmail())
                .password(requestJoinMember.getPassword())
                .name(requestJoinMember.getName())
                .nickname(requestJoinMember.getNickname())
                .tel(requestJoinMember.getTel())
                .zipCode(requestJoinMember.getZipCode())
                .address(requestJoinMember.getAddress())
                .addressDetail(requestJoinMember.getAddressDetail())
                .type(joinType)
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
