package com.lion.core;

import java.util.Map;

/**
 * @description:
 * @author: Mr.Liu
 * @create: 2020-02-16 15:10
 */
public interface IResultData<T> {

    public String getMessage();

    public Integer getStatus();

    public String getExceptionMessage();

    public T getData();
}