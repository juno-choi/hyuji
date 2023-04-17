package com.juno.hyugi.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LogAspect {
    private final Environment env;

    @Around("execution(* com.juno.hyugi.service..*.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        String requestURI = ((ServletRequestAttributes) requestAttributes).getRequest().getRequestURI();
        String method = ((ServletRequestAttributes) requestAttributes).getRequest().getMethod();
        Long loginMemberId = (Long) requestAttributes.getAttribute(env.getProperty("normal.login.attribute"), 0);

        log.info("[ member = {}, requestUri = ({}) {}, method = {} ]", loginMemberId, method, requestURI, pjp.getSignature().toShortString());

        return pjp.proceed();
    }
}
