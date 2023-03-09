package com.juno.normalapi.service;

import com.juno.normalapi.domain.dto.RequestBoard;
import com.juno.normalapi.domain.entity.Member;
import com.juno.normalapi.domain.vo.BoardListVo;
import com.juno.normalapi.domain.vo.BoardVo;
import com.juno.normalapi.exception.UnauthorizedException;
import com.juno.normalapi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.client.HttpClientErrorException.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService{
    private final Environment env;
    private final MemberRepository memberRepository;

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

    @Override
    public BoardVo postBoard(RequestBoard requestBoard, HttpServletRequest request) {
        Long loginUserId = (Long) request.getAttribute(env.getProperty("normal.login.attribute"));
        log.info("{} user post board", loginUserId);


        Member findMember = memberRepository.findById(loginUserId).orElseThrow(() -> new UnauthorizedException("잘못된 접근입니다."));

        return BoardVo.builder()
                .boardId(1L)
                .memberId(1L)
                .writer("테스터")
                .title("제목")
                .content("내용")
                .regDate(LocalDateTime.now())
                .build();
    }
}
