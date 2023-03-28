package com.juno.normalapi.domain.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestBoard {
    @NotNull(message = "title은 필수 값입니다.")
    private String title;
    @NotNull(message = "content는 필수 값입니다.")
    private String content;
}
