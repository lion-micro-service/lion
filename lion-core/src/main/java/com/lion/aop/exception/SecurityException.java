package com.lion.aop.exception;

import com.lion.core.IResultData;
import com.lion.core.ResultData;
import com.lion.core.common.enums.ResultDataState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author mr.liu
 * @title: SecurityException
 * @projectName lion
 * @description: SecurityException
 * @date 2020/7/29下午3:58
 */
@RestController
@ConditionalOnClass({HttpServletRequest.class})
public class SecurityException implements ErrorController {

    private static final String PATH = "/security/error";

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping(value = PATH)
    public IResultData error(HttpServletRequest request, HttpServletResponse response) {
        WebRequest requestAttributes = new ServletWebRequest(request);
        Map<String, Object> errorMap = errorAttributes.getErrorAttributes(requestAttributes, ErrorAttributeOptions.defaults());
        ResultData resultData = new ResultData();
        resultData.setData(errorMap);
        resultData.setStatus(ResultDataState.ERROR.getKey());
//        resultData.setMessage();
        response.setStatus(HttpServletResponse.SC_OK);
        return resultData;
    }
    @Override
    public String getErrorPath() {
        return PATH;
    }
}
