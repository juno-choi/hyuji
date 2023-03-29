package com.juno.normalapi.domain.dto.member;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestLoginMember {
    private String email;
    private String password;
}
