package com.icare.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.icare.service.*.*(..))")
    public void logAspectLogic() {}

    @Around("logAspectLogic()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        var signature = (MethodSignature) point.getSignature();
        var className = signature.getDeclaringType().getSimpleName();
        var methodName = signature.getName();

        logStartAction(className, methodName, signature.getParameterNames(), point.getArgs());

        Object response;
        long start = System.currentTimeMillis();
        try {
            response = point.proceed();
        } catch (Throwable throwable) {
            logExceptionAction(className, methodName, throwable);
            throw throwable;
        }
        long executionTime = System.currentTimeMillis() - start;

        logEndAction(className, methodName, signature.getReturnType(), response, executionTime);
        return response;
    }

    private void logStartAction(String className, String methodName, String[] paramNames, Object[] args) {
        var paramMap = buildParameters(paramNames, args);
        var paramsString = paramMap.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining(", ", "[", "]"));
        log.info("ActionLog.{}.{}.start.parameters:{}", className, methodName, paramsString);
    }

    private void logEndAction(String className, String methodName, Class<?> returnType, Object response, long executionTime) {
        if (void.class.equals(returnType)) {
            log.info("ActionLog.{}.{}.end.duration:[{}ms]", className, methodName, executionTime);
            return;
        }
        log.info("ActionLog.{}.{}.end.response:[{}].duration:[{}ms]", className, methodName, response, executionTime);
    }

    private void logExceptionAction(String className, String methodName, Throwable throwable) {
        log.error("ActionLog.{}.{}.{}.message:[{}]", className, methodName, throwable.getClass().getSimpleName(), throwable.getMessage());
    }

    private Map<Object, Object> buildParameters(String[] paramNames, Object[] args) {
        var paramMap = new LinkedHashMap<>();
        for (int i = 0; i < paramNames.length; i++) {
            paramMap.put(paramNames[i], args[i]);
        }
        return paramMap;
    }
}