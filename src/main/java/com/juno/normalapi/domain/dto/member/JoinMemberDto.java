package com.juno.normalapi.domain.dto.member;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class JoinMemberDto {
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String tel;
    private String zipCode;
    private String address;
    private String addressDetail;
}
