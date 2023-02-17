package com.juno.normalapi.service;

import com.juno.normalapi.domain.vo.BoardListVo;
import com.juno.normalapi.domain.vo.BoardVo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService{
    @Override
    public BoardListVo getBoardList() {
        List<BoardVo> list = new ArrayList<>();
        BoardVo boardVo = BoardVo.builder()
                .boardId(1L)
                .title("테스트 제목")
                .regDate(LocalDateTime.now().minusMinutes(30L))
                .writer("테스터")
                .build();

        BoardVo boardVo2 = BoardVo.builder()
                .boardId(2L)
                .title("테스트 제목2")
                .regDate(LocalDateTime.now())
                .writer("테스터2")
                .build();

        list.add(boardVo);
        list.add(boardVo2);

        list = list.stream()
                .sorted((s1, s2) -> Long.compare(s2.getBoardId(), s1.getBoardId()))
                .collect(Collectors.toList());

        return BoardListVo.builder()
                .boardList(list)
                .build();
    }
}
