package com.juno.normalapi.domain.vo.member;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.juno.normalapi.domain.enums.JoinType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MemberVo {
    private Long id;
    private String email;
    private String name;
    private String nickname;
    private String tel;
    private JoinType joinType;
    private String zipCode;
    private String address;
    private String addressDetail;
}
