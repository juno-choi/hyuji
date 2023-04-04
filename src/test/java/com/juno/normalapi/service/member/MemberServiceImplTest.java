package com.juno.normalapi.service.member;

import com.juno.normalapi.domain.dto.member.JoinMemberDto;
import com.juno.normalapi.domain.entity.member.Member;
import com.juno.normalapi.domain.enums.JoinType;
import com.juno.normalapi.domain.vo.member.MemberVo;
import com.juno.normalapi.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceImplTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("유효하지 않은 회원 번호는 조회에 실패한다.")
    void getMemberFail1(){
        // given
        // when
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> memberService.getMember(0L));
        // then
        assertEquals("유효하지 않은 회원입니다.", ex.getMessage());
    }

    @Test
    @DisplayName("회원 상세 조회에 성공한다.")
    void getMemberSuccess1(){
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
        MemberVo findMember = memberService.getMember(saveMember.getId());

        // then
        assertEquals(saveMember.getId(), findMember.getId());
    }
}