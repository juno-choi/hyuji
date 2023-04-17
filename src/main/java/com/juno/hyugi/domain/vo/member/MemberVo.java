package com.juno.hyugi.domain.vo.member;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.juno.hyugi.domain.enums.JoinType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

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
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
