package com.juno.normalapi.service.board;

import com.juno.normalapi.domain.dto.RequestBoard;
import com.juno.normalapi.domain.entity.Board;
import com.juno.normalapi.domain.entity.Member;
import com.juno.normalapi.domain.vo.BoardListVo;
import com.juno.normalapi.domain.vo.BoardVo;
import com.juno.normalapi.exception.UnauthorizedException;
import com.juno.normalapi.repository.board.BoardRepositoryCustom;
import com.juno.normalapi.repository.board.ReplyRepository;
import com.juno.normalapi.repository.member.MemberRepository;
import com.juno.normalapi.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService{
    private final Environment env;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final BoardRepositoryCustom boardRepositoryCustom;
    private final ReplyRepository replyRepository;

    @Transactional
    @Override
    public BoardVo postBoard(RequestBoard requestBoard, HttpServletRequest request) {
        Long loginUserId = (Long) request.getAttribute(env.getProperty("normal.login.attribute"));

        Member findMember = memberRepository.findById(loginUserId).orElseThrow(() -> new UnauthorizedException("잘못된 접근입니다."));
        Board saveBoard = boardRepository.save(Board.of(findMember, requestBoard.getTitle(), requestBoard.getContent()));

        return BoardVo.builder()
                .boardId(saveBoard.getId())
                .memberId(saveBoard.getMember().getMemberId())
                .title(saveBoard.getTitle())
                .content(saveBoard.getContent())
                .writer(findMember.getNickname())
                .replyCount(0L)
                .createdAt(saveBoard.getCreatedAt())
                .build();
    }

    @Override
    public BoardListVo getBoardList(Pageable pageable, HttpServletRequest request) {
        Page<BoardVo> page = boardRepositoryCustom.findAll(pageable);

        return BoardListVo.builder()
                .totalPage(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .numberOfElements(page.getNumberOfElements())
                .last(page.isLast())
                .empty(page.isEmpty())
                .list(page.getContent())
                .build();
    }

    @Override
    public BoardVo getBoard(Long boardId, HttpServletRequest request) {
        Board findBoard = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("유효하지 않은 게시판 번호입니다.")
        );

        Long replyCount = Long.valueOf(replyRepository.countByBoardId(findBoard.getId()));

        return BoardVo.builder()
                .boardId(findBoard.getId())
                .title(findBoard.getTitle())
                .content(findBoard.getContent())
                .writer(findBoard.getMember().getNickname())
                .replyCount(replyCount)
                .createdAt(findBoard.getCreatedAt())
                .build();
    }
}
