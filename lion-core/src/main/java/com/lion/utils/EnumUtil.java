package com.lion.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.lion.core.IEnum;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author mr.liu
 * @Description:
 * @date 2020/9/15下午2:20
 */
public class EnumUtil {

    /**
     * 获得指定包下面的所有的枚举类的值
     *
     * @param packageName
     * @return
     */
    public static Map<String, Object> getAllEnumsInPackage(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends IEnum>> allClasses = reflections.getSubTypesOf(IEnum.class);
        Map<String, Object> result = new HashMap<String, Object>();
        allClasses.forEach(t -> {
            try {
                List<String> list = new ArrayList<String>();
                Method method = t.getMethod("values");
                IEnum inter[] = (IEnum[]) method.invoke(null);
                for (IEnum ienum : inter) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    list.add(objectMapper.writeValueAsString(ienum.jsonValue()));
                }
                result.put(t.getName(),list);
            }catch (Exception exception){

            }
        });

        return result;
    }

    public static void main(String[] args) {
        EnumUtil.getAllEnumsInPackage("com.lion")
                .forEach((k, v) -> System.out.println(k + "=" + v));
    }
}
