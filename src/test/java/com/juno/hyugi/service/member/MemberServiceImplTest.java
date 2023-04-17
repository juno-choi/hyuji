package com.juno.hyugi.service.member;

import com.juno.hyugi.domain.dto.member.JoinMemberDto;
import com.juno.hyugi.domain.entity.member.Member;
import com.juno.hyugi.domain.enums.JoinType;
import com.juno.hyugi.domain.vo.member.MemberVo;
import com.juno.hyugi.exception.UnauthorizedException;
import com.juno.hyugi.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@SpringBootTest
class MemberServiceImplTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    private MockHttpServletRequest request = new MockHttpServletRequest();

    @Test
    @DisplayName("유효하지 않은 회원 번호는 조회에 실패한다.")
    void getMemberByIdFail1(){
        // given
        // when
        Throwable throwable = catchThrowable(() -> memberService.getMemberById(0L));
        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효하지 않은 회원입니다.");
    }

    @Test
    @DisplayName("회원 상세 조회에 성공한다.")
    void getMemberByIdSuccess1(){
        // given
        JoinMemberDto joinMemberDto = JoinMemberDto.builder()
                .email("detail@naver.com")
                .password("test123!")
                .name("상세")
                .nickname("상세회원")
                .tel("01012341234")
                .zipCode("12345")
                .address("경기도 성남시 중원구 자혜로17번길 16")
                .addressDetail("상세 주소")
                .build();
        Member saveMember = memberRepository.save(Member.of(joinMemberDto, JoinType.EMAIL));

        // when
        MemberVo findMember = memberService.getMemberById(saveMember.getId());

        // then
        assertThat(findMember.getId()).isEqualTo(saveMember.getId());
    }

    @Test
    @DisplayName("유효하지 않은 회원은 조회에 실패한다.")
    void getMemberFail1(){
        // given
        request.setAttribute("loginMemberId", "0");
        // when
        Throwable throwable = catchThrowable(() -> memberService.getMember(request));
        // then
        assertThat(throwable).isInstanceOf(UnauthorizedException.class)
                .hasMessage("잘못된 접근입니다.");
    }
}