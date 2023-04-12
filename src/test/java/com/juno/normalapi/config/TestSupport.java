package com.juno.normalapi.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juno.normalapi.domain.dto.member.JoinMemberDto;
import com.juno.normalapi.domain.entity.member.Member;
import com.juno.normalapi.domain.enums.JoinType;
import com.juno.normalapi.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Disabled
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional(readOnly = true)
public class TestSupport extends WebSecurityConfigurerAdapter {
    protected final String ACCESS_TOKEN = "Bearer test";

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mock;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected RedisTemplate<String, String> redisTemplate;

    @Autowired
    protected Environment env;

    protected String convertToString(Object dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    @BeforeAll
    void makeTestMemberProcess() {
        String testEmail = env.getProperty("normal.test.email");
        if(memberRepository.findByEmail(testEmail).isPresent()){
            return ;
        }
        Member saveMember = memberRepository.save(
                Member.of(JoinMemberDto.builder()
                        .name("테스터")
                        .address("주소")
                        .addressDetail("상세주소")
                        .email(testEmail)
                        .nickname("테스터닉네임")
                        .tel("01012341234")
                        .zipCode("우편번호")
                        .password("qwer1234!")
                        .build(), JoinType.EMAIL));

        Long memberId = saveMember.getId();
        redisTemplate.opsForHash().put("test", "access_token", String.valueOf(memberId));
    }
}
