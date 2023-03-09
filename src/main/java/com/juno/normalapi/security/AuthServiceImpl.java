package com.juno.normalapi.security;

import com.juno.normalapi.domain.entity.Member;
import com.juno.normalapi.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("회원 인증 확인 = {}", email);
        Member findMember = memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("아이디 혹은 비밀번호를 확인해주세요!"));
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(findMember.getRole()));
        return new User(String.valueOf(findMember.getMemberId()), findMember.getPassword(), true, true, true, true, authorities);
    }
}
