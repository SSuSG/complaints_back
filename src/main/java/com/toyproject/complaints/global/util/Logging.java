package com.toyproject.complaints.global.util;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class Logging {
    @Around("execution(* com.toyproject.complaints..*(..))")
    public Object doLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info("[현재] : {}",proceedingJoinPoint.getSignature());
        return proceedingJoinPoint.proceed();
    }
}
