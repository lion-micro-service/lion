package com.lion.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lion.core.LionObjectMapper;

/**
 * @description: json序列化工具
 * @author: Mr.Liu
 * @create: 2020-01-23 10:26
 */
public class JsonUtil {

    public static String convertToJsonString(final Object object) throws JsonProcessingException {
        LionObjectMapper lionObjectMapper = LionObjectMapper.getInstance();
        return lionObjectMapper.writeValueAsString(object);
    }
}
