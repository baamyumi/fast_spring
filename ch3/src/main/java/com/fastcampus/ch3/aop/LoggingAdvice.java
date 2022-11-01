package com.fastcampus.ch3.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component  //Advice도 Bean으로 등록이 되어야한다.
@Aspect
public class LoggingAdvice {
    @Around("execution(* com.fastcampus.ch3.aop.MyMath.*(..))*") //pointcut - 부가기능이 적용될 메서드의 패턴
    public Object methodCallLog(ProceedingJoinPoint pjp) throws Throwable{
        long start = System.currentTimeMillis();
        System.out.println("<<[start] " + pjp.getSignature().getName()+ Arrays.toString(pjp.getArgs()));
        
        Object result = pjp.proceed();

        System.out.println("result = " + result);
        System.out.println("[end]>> "+ (System.currentTimeMillis() - start)+ "ms");
        return result;
    }
}
