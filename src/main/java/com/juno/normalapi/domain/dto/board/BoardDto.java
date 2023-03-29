package com.juno.normalapi.domain.dto.board;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDto {
    @NotNull(message = "title은 필수 값입니다.")
    private String title;
    @NotNull(message = "content는 필수 값입니다.")
    private String content;
}
