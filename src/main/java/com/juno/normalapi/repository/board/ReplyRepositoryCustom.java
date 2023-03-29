package com.juno.normalapi.repository.board;

import com.juno.normalapi.domain.vo.ReplyVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReplyRepositoryCustom {
    Page<ReplyVo> findAll(Long boardId, Pageable pageable);
}
