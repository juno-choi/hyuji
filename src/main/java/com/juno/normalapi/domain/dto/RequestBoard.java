package com.juno.normalapi.domain.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestBoard {
    private String title;
    private String content;
}
