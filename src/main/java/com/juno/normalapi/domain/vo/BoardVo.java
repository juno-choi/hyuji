package com.juno.normalapi.domain.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
public class BoardVo {
    private Long boardId;
    private Long memberId;
    private String title;
    private String content;
    private String writer;
    private Long replyCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
