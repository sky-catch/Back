package com.example.core.aop.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class LogAspect {

    @Pointcut("execution(* *..*Controller.*(..))")
    public void controllerLog(){}

    @Around("controllerLog()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        log.info("[CONTENT] {}.{}", signature.getDeclaringType().getSimpleName(), signature.getName());
        return joinPoint.proceed();
    }

}
