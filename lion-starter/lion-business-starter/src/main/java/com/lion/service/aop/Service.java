package com.lion.service.aop;

import com.lion.core.PageResultData;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 *
 */

@Aspect
@Component("springMvcServiceAspect")
public class Service {

    private static Logger logger = LoggerFactory.getLogger(Service.class);

    @Around(value = "execution(org.springframework.data.domain.Page com.lion..*.service.impl.*.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object obj = pjp.proceed();
        if (obj instanceof Page){
            Page<?> page = (Page<?>) obj;
            PageResultData pageResultData = new PageResultData(page.getContent(),page.getPageable(),page.getTotalElements());
            return pageResultData;
        }
        return obj;
    }
}
