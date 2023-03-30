package com.juno.normalapi.security.oauth2;

import com.juno.normalapi.domain.entity.member.Member;
import com.juno.normalapi.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class Oauth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("oauth2 start [type = {}]", registrationId);
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Member member = null;
        // kakao
        if(registrationId.equals("kakao")){
            Long snsId = (Long) attributes.get("id");
            Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
            String nickname = (String) ((HashMap) kakao_account.get("profile")).get("nickname");
            String email = (Boolean) kakao_account.get("has_email") ? (String) kakao_account.get("email") : snsId+"@kakao.mail";

            member = memberRepository.findByEmail(email).orElse(Member.ofKakao(snsId, nickname, email));
            // 최근 접속일 업데이트
            member.updateConnect();
            memberRepository.save(member);
        }

        // TODO naver 등

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(member.getRole())), attributes, "id");
    }
}
