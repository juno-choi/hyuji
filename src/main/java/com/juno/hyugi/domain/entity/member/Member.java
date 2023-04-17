package com.juno.hyugi.domain.entity.member;

import com.juno.hyugi.domain.dto.member.JoinMemberDto;
import com.juno.hyugi.domain.enums.JoinType;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
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
    private String password;
    private String name;
    @Column(nullable = false)
    private String nickname;
    private String tel;

    private JoinType type;
    private String zipCode;
    private String address;
    private String addressDetail;
    private String role;
    @Column(unique = true)
    private Long snsId;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime lastConnect;

    // 일반 유저
    public static Member of(JoinMemberDto joinMemberDto, JoinType joinType){
        LocalDateTime now = LocalDateTime.now();
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
                .createdAt(now)
                .modifiedAt(now)
                .lastConnect(now)
                .build();
    }

    // 카카오 유저
    public static Member ofKakao(Long snsId, String nickname, String email){
        LocalDateTime now = LocalDateTime.now();
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .type(JoinType.KAKAO)
                .role("USER")
                .createdAt(now)
                .modifiedAt(now)
                .lastConnect(now)
                .snsId(snsId)
                .build();
    }

    public void encryptPassword(Member member, BCryptPasswordEncoder encoder){
        this.password = encoder.encode(member.getPassword());
    }

    public void updateConnect(){
        this.lastConnect = LocalDateTime.now();
    }
}
