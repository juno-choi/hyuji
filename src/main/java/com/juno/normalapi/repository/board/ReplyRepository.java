package com.juno.normalapi.repository.board;

import com.juno.normalapi.domain.entity.board.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    int countByBoardId(Long boardId);
}
