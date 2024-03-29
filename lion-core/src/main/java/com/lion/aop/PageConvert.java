package com.lion.aop;

import com.lion.core.PageResultData;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * @author mr.liu
 * @title: PageConvert
 * @description: 将org.springframework.data.domain.Page转化为com.lion.core.IResultData
 * @date 2020/8/18上午11:38
 */
@Aspect
@Component
@ConditionalOnClass(Page.class)
public class PageConvert {

    @Around(value = "(execution(org.springframework.data.domain.Page com.lion..*.expose..*.*(..)) " +
            "|| execution(org.springframework.data.domain.PageImpl com.lion..*.expose..*.*(..)) " +
            "|| execution(org.springframework.data.domain.Page com.lion..*.service..*.*(..))" +
            "|| execution(org.springframework.data.domain.PageImpl com.lion..*.service..*.*(..)))" +
            "&& execution(public * com.lion..*.*(..))")
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
