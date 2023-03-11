package com.juno.normalapi.service.board;

import com.juno.normalapi.domain.dto.RequestBoard;
import com.juno.normalapi.domain.entity.Board;
import com.juno.normalapi.domain.entity.Member;
import com.juno.normalapi.domain.vo.BoardListVo;
import com.juno.normalapi.domain.vo.BoardVo;
import com.juno.normalapi.exception.UnauthorizedException;
import com.juno.normalapi.repository.member.MemberRepository;
import com.juno.normalapi.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService{
    private final Environment env;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    @Override
    public BoardListVo getBoardList() {
        List<BoardVo> list = new ArrayList<>();
        BoardVo boardVo = BoardVo.builder()
                .boardId(1L)
                .title("테스트 제목")
                .createdAt(LocalDateTime.now().minusMinutes(30L))
                .writer("테스터")
                .build();

        BoardVo boardVo2 = BoardVo.builder()
                .boardId(2L)
                .title("테스트 제목2")
                .createdAt(LocalDateTime.now())
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

    @Transactional
    @Override
    public BoardVo postBoard(RequestBoard requestBoard, HttpServletRequest request) {
        Long loginUserId = (Long) request.getAttribute(env.getProperty("normal.login.attribute"));
        log.info("{} user post board", loginUserId);

        Member findMember = memberRepository.findById(loginUserId).orElseThrow(() -> new UnauthorizedException("잘못된 접근입니다."));
        Board saveBoard = boardRepository.save(Board.of(findMember, requestBoard.getTitle(), requestBoard.getContent()));

        return BoardVo.builder()
                .boardId(saveBoard.getId())
                .memberId(saveBoard.getMember().getMemberId())
                .title(saveBoard.getTitle())
                .content(saveBoard.getContent())
                .writer(findMember.getNickname())
                .createdAt(saveBoard.getCreatedAt())
                .build();
    }
}
