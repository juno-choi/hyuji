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
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Comment("작성자 id")
    private Long memberId;

    private String title;
    private String content;

    private LocalDateTime modifiedAt;
    private LocalDateTime createdAt;

}
