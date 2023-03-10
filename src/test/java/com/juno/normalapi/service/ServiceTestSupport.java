package com.juno.normalapi.service;

import com.juno.normalapi.domain.dto.RequestJoinMember;
import com.juno.normalapi.domain.entity.Member;
import com.juno.normalapi.domain.enums.JoinType;
import com.juno.normalapi.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import java.util.Optional;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServiceTestSupport {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    protected Environment env;

    protected Member member;

    @BeforeAll
    void setup(){
        String email = env.getProperty("normal.test.email");
        Member member = null;
        Optional<Member> findMember = memberRepository.findByEmail(email);

        if(findMember.isEmpty()){
            member = memberRepository.save(Member.of(RequestJoinMember.builder()
                    .email(email)
                    .password("test1234!")
                    .tel("01012341234")
                    .name("테스터")
                    .zipCode("123")
                    .nickname("테스터 닉네임")
                    .addressDetail("상세주소")
                    .address("경기도 성남시 판교")
                    .build(), JoinType.EMAIL));
        }else{
            member = findMember.get();
        }

        this.member = member;
    }
}
