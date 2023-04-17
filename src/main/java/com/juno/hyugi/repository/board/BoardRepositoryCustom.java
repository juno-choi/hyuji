package com.juno.hyugi.repository.board;

import com.juno.hyugi.domain.vo.board.BoardVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    Page<BoardVo> findAll(Pageable pageable);
}
