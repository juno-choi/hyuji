package com.juno.normalapi.repository.board;

import com.juno.normalapi.domain.vo.BoardVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    Page<BoardVo> findAll(Pageable pageable);
}
