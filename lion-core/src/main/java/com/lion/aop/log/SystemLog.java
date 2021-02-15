package com.lion.aop.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lion.aop.exception.ExceptionData;
import com.lion.aop.exception.LoadBalancerException;
import com.lion.core.persistence.entity.BaseEntity;
import com.lion.utils.CurrentUserUtil;
import com.lion.utils.id.SnowflakeUtil;
import io.micrometer.core.instrument.util.IOUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.firewall.FirewalledRequest;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * 系统日志（记录请求数据和返回数据）
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SystemLog {

    private static Logger logger = LoggerFactory.getLogger(SystemLog.class);

    @Around(value = "(@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)"+
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)"+
            "|| @annotation(org.springframework.web.bind.annotation.PatchMapping) )" +
            "&& execution(public * com.lion..*.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        LocalDateTime startDateTime = LocalDateTime.now();
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        com.lion.annotation.SystemLog systemLog = method.getAnnotation(com.lion.annotation.SystemLog.class);
//        if ((Objects.nonNull(systemLog) && systemLog.log()) || Objects.isNull(systemLog)){
//            systemLog(method,startDateTime);
//        }
        Object object = pjp.proceed();
//        if ((Objects.nonNull(systemLog) && systemLog.log()) || Objects.isNull(systemLog)){
//            SystemLogData systemLogData = SystemLogDataUtil.get();
//            systemLogData.setResponseData(object);
//            ObjectMapper objectMapper = new ObjectMapper();
//            logger.debug(objectMapper.writeValueAsString(systemLogData));
//        }
        return object;

    }

    private void systemLog(Method method,LocalDateTime startDateTime) throws JsonProcessingException {
        SystemLogData systemLogData = new SystemLogData();
        systemLogData.setTrackId(SnowflakeUtil.getId());
        Map<String,Object> user = CurrentUserUtil.getCurrentUser(false);
        if(Objects.nonNull(user) && user.containsKey("id")){
            systemLogData.setCurrentUserId(Long.valueOf(String.valueOf(user.get("id"))));
        }
        systemLogData.setSequenceNumber(1);
        systemLogData.setTarget(method.toGenericString());
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
        systemLogData.setUri(request.getRequestURI());
        systemLogData.setIp(request.getRemoteAddr());
        Map<String, String[]> parameter = request.getParameterMap();
        ObjectMapper objectMapper = new ObjectMapper();
        systemLogData.setParameter(parameter);
        if(Objects.nonNull(request.getContentType()) && (request.getContentType().indexOf("application/x-www-form-urlencoded")>-1 || request.getContentType().indexOf("application/json")>-1)){
            String body="";
            try {
                body = new String (((ContentCachingRequestWrapper)request).getContentAsByteArray() , "UTF-8");
                systemLogData.setBody(objectMapper.readValue(body,Map.class));
            } catch (IOException e) {
            }
        }
        LocalDateTime endDateTime = LocalDateTime.now();
        Duration duration = Duration.between(startDateTime,endDateTime );
        systemLogData.setExecuteTime(duration.toMillis());
        SystemLogDataUtil.set(systemLogData);
    }
}

