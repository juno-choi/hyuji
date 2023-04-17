package com.juno.hyugi.repository.board;

import com.juno.hyugi.config.QueryDslConfig;
import com.juno.hyugi.domain.vo.board.BoardVo;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.juno.hyugi.domain.entity.board.QBoard.board;
import static com.juno.hyugi.domain.entity.board.QReply.reply;


@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{
    private final QueryDslConfig qd;


    @Override
    public Page<BoardVo> findAll(Pageable pageable) {
        List<BoardVo> content = qd.query()
                .select(Projections.constructor(BoardVo.class,
                        board.id,
                        board.member.id,
                        board.title,
                        board.content,
                        board.member.nickname,
                        ExpressionUtils.as(
                            JPAExpressions.select(reply.count())
                                    .from(reply)
                                    .where(reply.board.id.eq(board.id)),
                            "replyCount"
                        ),
                        board.createdAt,
                        board.modifiedAt
                        ))
                .from(board)
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = qd.query()
                .from(board)
                .stream().count();
        return new PageImpl<>(content, pageable, total);
    }
}
