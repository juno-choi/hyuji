package com.juno.normalapi.domain.entity.member;

import com.juno.normalapi.domain.dto.member.JoinMemberDto;
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
    @Column(name = "member_id")
    private Long id;

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
    public static Member of(JoinMemberDto joinMemberDto, JoinType joinType){
        return Member.builder()
                .email(joinMemberDto.getEmail())
                .password(joinMemberDto.getPassword())
                .name(joinMemberDto.getName())
                .nickname(joinMemberDto.getNickname())
                .tel(joinMemberDto.getTel())
                .zipCode(joinMemberDto.getZipCode())
                .address(joinMemberDto.getAddress())
                .addressDetail(joinMemberDto.getAddressDetail())
                .type(joinType)
                .role("USER")
                .build();
    }

    public void encryptPassword(Member member, BCryptPasswordEncoder encoder){
        this.password = encoder.encode(member.getPassword());
    }

    public void changeRole(String role){
        this.role = role;
    }
}
