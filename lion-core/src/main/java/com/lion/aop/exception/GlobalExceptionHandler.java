package com.lion.aop.exception;

import com.lion.core.IResultData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description: 全局异常处理
 * @author: Mr.Liu
 * @create: 2020-02-12 22:22
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public IResultData exception(Exception e) {
        return ExceptionData.instance(e);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseBody
//    public IResultData exception(MethodArgumentNotValidException e) {
//        return ExceptionData.instance(e);
//    }
}
