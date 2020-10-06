package com.lion.annotation.swagger;

import com.lion.core.IResultData;
import com.lion.core.ResultData;

/**
 * @description: 自定义swagger返回数据结构
 * @author: mr.liu
 * @create: 2020-10-06 15:08
 **/
public @interface LionApiResponses {

    /**
     * 返回数据结构
     * @return
     */
    public Class<? extends IResultData> returType() default ResultData.class;

    /**
     * 返回的数据（会放置在IResultData.data下面）
     * @return
     */
    LionApiResponse[] data() default {};
}
