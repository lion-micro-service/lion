package com.lion.aop.exception;

import com.lion.exception.BusinessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 统一异常处理
 */

@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestulException {

    private static Logger logger = LoggerFactory.getLogger(RestulException.class);

    @Around(value = "(@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)"+
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)"+
            "|| @annotation(org.springframework.web.bind.annotation.PatchMapping))" +
            "&& !execution(* org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint.checkToken(..))")
    public Object around(ProceedingJoinPoint pjp) {
        Object invokeResult = null;
        Object[] args = pjp.getArgs();
        Object controllerClass = pjp.getTarget();
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        try {
            Arrays.stream(args).forEach(arg ->{
                if(arg instanceof BindingResult){
                    BindingResult bindingResult = (BindingResult) arg;
                    bindingResult.getFieldErrors().forEach(error ->{
                        BusinessException.throwException(error.getDefaultMessage());
                    });
                }
            });
            invokeResult = pjp.proceed();
        } catch (Throwable e) {
            return ExceptionData.instance(e);
        }
        return invokeResult;
    }
}
