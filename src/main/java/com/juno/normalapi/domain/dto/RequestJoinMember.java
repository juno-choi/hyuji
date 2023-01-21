package com.juno.normalapi.domain.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestJoinMember {
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String tel;
    private String zipCode;
    private String address;
    private String addressDetail;
}
