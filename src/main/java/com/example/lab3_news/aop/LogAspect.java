package com.example.lab3_news.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import static com.example.lab3_news.Lab3NewsApplication.log;

/**
 * Class for enable aspect logging.
 */
@Component
@Aspect
public class LogAspect {



    @Before("execution(* com.example.lab3_news.*.*.*())")
    public void logMethod (JoinPoint joinPoint) {
        log.info("LOG.INFO FROM LOG ASPECT Method " + joinPoint.getSignature().getName() + " started");
    }

}
