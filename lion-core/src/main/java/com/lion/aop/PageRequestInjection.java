package com.lion.aop;

import com.alibaba.fastjson.JSONObject;
import com.lion.constant.GlobalConstant;
import com.lion.core.LionPage;
import com.lion.core.persistence.JpqlParameter;
import org.apache.commons.lang3.math.NumberUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author mr.liu
 * @title: PageRequestInjection
 * @description: LionPage查询参数注入
 * @date 2020/8/19上午11:01
 */
@Aspect
@Component
@ConditionalOnClass(Servlet.class)
@Order(Ordered.LOWEST_PRECEDENCE)
public class PageRequestInjection {

    @Around(value = "(@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)"+
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)"+
            "|| @annotation(org.springframework.web.bind.annotation.PatchMapping))" +
            "&& (execution(public * com.lion..*.*(..)) || execution(public * com.smartlinks..*.*(..)))" )
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object invokeResult = null;
        Object[] args = pjp.getArgs();
        for (int i =0; i< args.length; i++){
            Object arg = args[i];
            if(arg instanceof PageRequest || arg instanceof LionPage){
                args[i] = new LionPage(getPage(),getSize(),Sort.unsorted());
            }
        }
        return pjp.proceed(args);
    }

    /**
     * 获取当前页码
     * @return
     */
    private Integer getPage(){
        HttpServletRequest request = getHttpServletRequest();
        if (Objects.isNull(request)){
            return 0;
        }
        String pageNumber = request.getParameter(GlobalConstant.PAGE_NUMBER);
        if (NumberUtils.isDigits(pageNumber)) {
            return Integer.valueOf(pageNumber) -1;
        }
        return 0;
    }

    /**
     * 获取分页大小
     * @return
     */
    private int getSize(){
        HttpServletRequest request = getHttpServletRequest();
        if (Objects.isNull(request)){
            return 30;
        }
        String pageSize = request.getParameter(GlobalConstant.PAGE_SIZE);
        if (NumberUtils.isDigits(pageSize)) {
            return Integer.valueOf(pageSize);
        }
        return 30;
    }

    private Integer getPageInfo(String key) {
        HttpServletRequest request = getHttpServletRequest();
        ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
        String requestBody = new String(requestWrapper.getContentAsByteArray());
        if (StringUtils.hasText(requestBody)) {
            JSONObject json = JSONObject.parseObject(requestBody);
            if (json.containsKey(key)) {
                return json.getInteger(key);
            }
        }
        return null;
    }

    private HttpServletRequest getHttpServletRequest(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if(Objects.isNull(requestAttributes)){
            return null;
        }
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
        return request;
    }
}
