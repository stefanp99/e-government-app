package com.stefan.egovernmentapp.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.stefan.egovernmentapp.services.ComplaintService.*(..))")
    public void logMethodCall(JoinPoint joinPoint) {
        logger.info("Method " + joinPoint.getSignature().getName() + " is called.");
    }
}
