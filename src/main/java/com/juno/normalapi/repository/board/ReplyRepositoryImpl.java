package com.juno.normalapi.repository.board;

import com.juno.normalapi.config.QueryDslConfig;
import com.juno.normalapi.domain.vo.ReplyVo;
import com.querydsl.core.types.Projections;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.juno.normalapi.domain.entity.QReply.*;

@Repository
@RequiredArgsConstructor
public class ReplyRepositoryImpl implements ReplyRepositoryCustom{
    private final QueryDslConfig qd;

    @Override
    public Page<ReplyVo> findAll(Long boardId, Pageable pageable) {
        List<ReplyVo> content = qd.query().select(Projections.constructor(ReplyVo.class,
                reply.id,
                reply.board.id,
                reply.content,
                reply.createdAt,
                reply.modifiedAt,
                reply.member.nickname
        ))
        .from(reply)
        .where(reply.board.id.eq(boardId))
        .orderBy(reply.id.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

        long total = qd.query()
                .from(reply)
                .stream()
                .count();
        return new PageImpl<>(content, pageable, total);
    }
}
