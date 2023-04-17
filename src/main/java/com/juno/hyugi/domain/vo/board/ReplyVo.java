package com.juno.hyugi.domain.vo.board;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReplyVo {
    private Long id;
    private Long boardId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String writer;

    public ReplyVo(Long id, Long boardId, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, String writer) {
        this.id = id;
        this.boardId = boardId;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.writer = writer;
    }

    public static ReplyVo of(Long replyId, Long boardId, String content, String writer) {
        LocalDateTime now = LocalDateTime.now();
        return new ReplyVo(replyId, boardId, content, now, now, writer);
    }
}
