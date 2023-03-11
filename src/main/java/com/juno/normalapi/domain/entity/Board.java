package com.juno.normalapi.domain.entity;

import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Comment("작성자 id")
    private Member member;

    private String title;
    private String content;

    private LocalDateTime modifiedAt;
    private LocalDateTime createdAt;

    private Board(Member member, String title, String content, LocalDateTime modifiedAt, LocalDateTime createdAt) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.modifiedAt = modifiedAt;
        this.createdAt = createdAt;
    }

    public static Board of(Member member, String title, String content){
        LocalDateTime now = LocalDateTime.now();
        return new Board(member, title, content, now, now);
    }
}
