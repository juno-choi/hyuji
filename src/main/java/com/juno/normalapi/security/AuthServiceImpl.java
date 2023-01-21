package com.juno.normalapi.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService{
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("회원 인증 확인");
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        return new User("유저", new BCryptPasswordEncoder().encode("비밀번호"), true, true, true, true, authorities);
    }
}
