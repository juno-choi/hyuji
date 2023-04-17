package com.juno.hyugi.security.oauth2;

import com.juno.hyugi.domain.entity.member.Member;
import com.juno.hyugi.repository.member.MemberRepository;
import com.juno.hyugi.security.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final MemberRepository memberRepository;
    private final AuthUtil authUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        Member member = null;
        Long memberId = 0L;

        // TODO kakao, naver 등 sns oauth를 사용하여 id 가져와야함.
        if(registrationId.equals("kakao")){
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            Long snsId = (Long) oAuth2User.getAttributes().get("id");
            member = memberRepository.findBySnsId(snsId).orElseThrow(()-> new IllegalArgumentException("유효하지 않은 회원입니다. 관리자에게 문의해주세요."));
        }
        memberId = member.getId();

        authUtil.responseToken(response, authorities, memberId);
    }
}
