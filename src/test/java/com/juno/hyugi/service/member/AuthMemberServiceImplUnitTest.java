package com.juno.hyugi.service.member;

import com.juno.hyugi.domain.dto.member.JoinMemberDto;
import com.juno.hyugi.domain.entity.member.Member;
import com.juno.hyugi.domain.enums.JoinType;
import com.juno.hyugi.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AuthMemberServiceImplUnitTest {
    @InjectMocks
    private AuthMemberServiceImpl authMemberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private Environment env;

    @Test
    @DisplayName("이미 가입한 이메일은 회원가입에 실패한다.")
    void joinFail1(){
        // given
        JoinMemberDto dto = JoinMemberDto.builder()
                .email("mail@mail.com")
                .name("test")
                .nickname("테스터")
                .build();
        Member member = Member.of(dto, JoinType.EMAIL);
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));

        // when
        Throwable throwable = catchThrowable(() -> authMemberService.join(dto));
        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 이메일 입니다.");
    }
}
