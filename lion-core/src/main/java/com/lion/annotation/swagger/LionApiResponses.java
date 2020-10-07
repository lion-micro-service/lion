package com.lion.annotation.swagger;

import com.lion.core.IResultData;
import com.lion.core.ResultData;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * @description: 自定义swagger返回数据结构
 * @author: mr.liu
 * @create: 2020-10-06 15:08
 **/
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface LionApiResponses {

    /**
     * 返回数据结构
     * @return
     */
    public Class<? extends IResultData> returType() default ResultData.class;

    /**
     * 返回的数据（会放置在IResultData.data下面）固定值（IResultData/ResultData/PageResultData）
     * @return
     */
    LionApiResponse[] data() default {};
}
