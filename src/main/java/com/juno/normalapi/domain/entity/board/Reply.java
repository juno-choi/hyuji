package com.juno.normalapi.domain.entity.board;

import com.juno.normalapi.domain.entity.member.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String content;
    private LocalDateTime modifiedAt;
    private LocalDateTime createdAt;

    public Reply(Member member, Board board, String content, LocalDateTime modifiedAt, LocalDateTime createdAt) {
        this.member = member;
        this.board = board;
        this.content = content;
        this.modifiedAt = modifiedAt;
        this.createdAt = createdAt;
    }

    public static Reply of(Member member, Board board, String content){
        LocalDateTime now = LocalDateTime.now();
        return new Reply(member, board, content, now, now);
    }
}
