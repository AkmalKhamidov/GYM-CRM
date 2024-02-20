package com.epamlearning.logging;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

import static com.epamlearning.GymcrmSecurityApplication.TRANSACTION_ID;


@Aspect
@Component
public class Logging {
    
    
    
    private static final Logger logger = LoggerFactory.getLogger("logging");

    @Before("execution(* com.epamlearning.controllers.*.*(..))")
    public void logControllerCall(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            logger.info("===== Transaction Started: =====");
            logger.info("Transaction Id: {}", request.getAttribute(TRANSACTION_ID));
            logger.info("Request URL: {}", request.getRequestURL());
            logger.info("Method: {}", request.getMethod());
            logger.info("Headers: {}", request.getHeaderNames());
            logger.info("Parameters: {}", request.getParameterMap());
            logger.info("Request body: {}", Arrays.toString(joinPoint.getArgs()));
            logger.info("Query String: {}", request.getQueryString());
            logger.info("IP: {}", request.getRemoteAddr());
            logger.info("Controller: {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        }
    }

    @AfterReturning(pointcut = "execution(* com.epamlearning.controllers.*.*(..))", returning = "result")
    public void logControllerResponse(Object result) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String transactionId = (String) request.getAttribute(TRANSACTION_ID);
            logger.info("Transaction Id: {}", transactionId);
            logger.info("Response: {}", result);
            logger.info("===== Transaction Ended =====");
        }
    }

    @Before("execution(* com.epamlearning.services.*.*(..))")
    public void logServiceMethodCall(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String transactionId = (String) request.getAttribute(TRANSACTION_ID);
                logger.info("=== Service method {} {} started (Transaction Id: {}) ===", ((MethodSignature) joinPoint.getSignature()).getReturnType(), joinPoint.getSignature().getName(), transactionId);
                logger.info("Parameters: ");
                String[] parameters = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
                Object[] arguments = joinPoint.getArgs();
                for (int i = 0; i < parameters.length; i++) {
                    logger.info("{}: {}", parameters[i], arguments[i].toString());
                }
            }
        }
    }

    @AfterReturning(pointcut = "execution(* com.epamlearning.services.*.*(..))", returning = "result")
    public void logServiceMethodResponse(Object result) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            logger.info("Result: {}", result);
            logger.info("=== Service method end ===");
        }
    }

    @AfterThrowing(pointcut = "execution(* com.epamlearning.controllers.*.*(..))", throwing = "e")
    public void logTransactionError(Exception e) {
        logger.error("Failed with error: {}", e.getMessage());
    }

    @AfterThrowing(pointcut = "execution(* com.epamlearning.services.*.*(..))", throwing = "e")
    public void logServiceMethodError(Exception e) {
        logger.error("Failed with error: {}", e.getMessage());
    }

    public void log(String message){
        logger.info(message);
    }
}
