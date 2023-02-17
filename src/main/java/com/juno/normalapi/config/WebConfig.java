package com.juno.normalapi.config;

import com.juno.normalapi.interceptor.TokenInterceptor;
import com.juno.normalapi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final Environment env;
    private final RedisTemplate redisTemplate;
    private final MemberRepository memberRepository;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor(env, redisTemplate, memberRepository))
                .order(1)
                .addPathPatterns("/v1/**")
                //.excludePathPatterns()
        ;
    }
}
