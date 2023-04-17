package com.juno.hyugi.repository.board;

import com.juno.hyugi.domain.vo.board.ReplyVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReplyRepositoryCustom {
    Page<ReplyVo> findAll(Long boardId, Pageable pageable);
}
