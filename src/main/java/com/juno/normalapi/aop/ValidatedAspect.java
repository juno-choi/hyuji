package com.juno.normalapi.aop;

import com.juno.normalapi.api.ResponseCode;
import com.juno.normalapi.api.ResponseError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ValidatedAspect {
    private final Environment env;

    @Around("execution(* com.juno.normalapi.controller..*.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        String type = pjp.getSignature().getDeclaringTypeName();
        String method = pjp.getSignature().getName();
        log.info("validation check... type = [{}], method = [{}]", type, method);
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        Long loginMemberId = (Long) requestAttributes.getAttribute(env.getProperty("normal.login.attribute"), 0);

        Object[] args = pjp.getArgs();
        for(Object a : args){
            if(a instanceof BindingResult){   // object type == BindingResult
                BindingResult bindingResult = (BindingResult) a;
                if(bindingResult.hasErrors()){  // 유효성 검사에 걸리는 에러가 존재한다면
                    log.error("[member = {}] validation error", loginMemberId);

                    List<String> errors = new ArrayList<>();
                    for(FieldError error : bindingResult.getFieldErrors()){
                        log.error("[parameter : {}] [message = {}]", error.getField(), error.getDefaultMessage());
                        errors.add(String.format("%s", error.getDefaultMessage()));
                    }

                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(
                                    ResponseError.<String>builder()
                                            .code(ResponseCode.FAIL)
                                            .message("요청 값을 확인해주세요.")
                                            .errors(errors)
                                            .build()
                            );
                }
            }
        }

        return pjp.proceed();
    }
}
