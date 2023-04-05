package com.juno.normalapi.service.board;

import com.juno.normalapi.domain.dto.board.BoardDto;
import com.juno.normalapi.domain.dto.board.ReplyDto;
import com.juno.normalapi.domain.entity.member.Member;
import com.juno.normalapi.exception.UnauthorizedException;
import com.juno.normalapi.repository.board.BoardRepository;
import com.juno.normalapi.repository.board.ReplyRepository;
import com.juno.normalapi.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BoardServiceImplUnitTest {
    @InjectMocks
    private BoardServiceImpl boardService;

    @Mock
    private Environment env;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private ReplyRepository replyRepository;

    private MockHttpServletRequest request = new MockHttpServletRequest();

    @Test
    @DisplayName("게시글 등록에 실패")
    void postBoardFail1() {
        // given
        BoardDto boardDto = BoardDto.builder()
                .title("제목")
                .content("내용")
                .build();

        given(env.getProperty(anyString())).willReturn("loginMemberId");
        request.setAttribute("loginMemberId", 0);

        // when
        Throwable throwable = catchThrowable(() -> boardService.postBoard(boardDto, request));

        // then
        assertThat(throwable).isInstanceOf(UnauthorizedException.class)
                .hasMessage("잘못된 접근입니다.");
    }
    
    @Test
    @DisplayName("유효하지 않은 회원은 댓글 등록에 실패")
    void postReplyFail1(){
        // given
        given(env.getProperty(anyString())).willReturn("loginMemberId");
        request.setAttribute("loginMemberId", 0);
        // when
        Throwable throwable = catchThrowable(() -> boardService.postReply(
                ReplyDto.builder()
                        .boardId(1L)
                        .content("테스트")
                        .build(),
                request
        ));
        // then
        assertThat(throwable).isInstanceOf(UnauthorizedException.class)
                .hasMessage("잘못된 접근입니다.");
    }

    @Test
    @DisplayName("유효하지 게시판은 댓글 등록에 실패")
    void postReplyFail2(){
        // given
        given(env.getProperty(anyString())).willReturn("loginMemberId");
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(Member.builder().build()));
        request.setAttribute("loginMemberId", 1L);

        // when
        Throwable throwable = catchThrowable(() -> boardService.postReply(
                ReplyDto.builder()
                        .boardId(1L)
                        .content("테스트")
                        .build(),
                request
        ));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효하지 않은 게시판입니다.");
    }
}