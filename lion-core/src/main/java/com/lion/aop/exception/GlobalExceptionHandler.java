package com.lion.aop.exception;

import com.lion.core.ResultData;
import com.lion.core.ResultDataState;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    public ResultData exception(Exception e) {
        return ExceptionData.instance(e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResultData exception(MethodArgumentNotValidException e) {
        ResultData resultData = new ResultData();
        resultData.setStatus(ResultDataState.LOGIC_ERROR.getKey());
        BindingResult bindingResult = e.getBindingResult();
        for(FieldError fieldError : bindingResult.getFieldErrors()){
            resultData.setMessage(fieldError.getDefaultMessage());
            return resultData;
        }
        return resultData;
    }
}
