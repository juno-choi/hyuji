package com.juno.normalapi.repository.board;

import com.juno.normalapi.domain.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
