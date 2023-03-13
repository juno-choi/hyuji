package com.juno.normalapi.repository.board;

import com.juno.normalapi.config.QueryDslConfig;
import com.juno.normalapi.domain.vo.BoardVo;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.juno.normalapi.domain.entity.QBoard.board;
import static com.juno.normalapi.domain.entity.QReply.reply;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{
    private final QueryDslConfig qd;


    @Override
    public Page<BoardVo> findAll(Pageable pageable) {
        List<BoardVo> content = qd.query()
                .select(Projections.constructor(BoardVo.class,
                        board.id,
                        board.member.memberId,
                        board.title,
                        board.content,
                        board.member.nickname,
                        ExpressionUtils.as(
                            JPAExpressions.select(reply.count())
                                    .from(reply)
                                    .where(reply.board.id.eq(board.id)),
                            "replyCount"
                        ),
                        board.createdAt
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
