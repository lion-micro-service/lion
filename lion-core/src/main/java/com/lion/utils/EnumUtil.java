package com.lion.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lion.core.IEnum;

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
     * @return
     */
    public static Map<String, Object> getAllEnumsInPackage(List<String> listEnum) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (Objects.isNull(listEnum)){
            return result;
        }
        listEnum.forEach(e->{
            try {
                Class c = Class.forName(e);
                List<String> list = new ArrayList<String>();
                Method method = c.getMethod("values");
                IEnum inter[] = (IEnum[]) method.invoke(null);
                for (IEnum ienum : inter) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    list.add(objectMapper.writeValueAsString(ienum.jsonValue()));
                }
                result.put(c.getName(),list);
            }
            catch (ClassNotFoundException | NoSuchMethodException classNotFoundException ) {
//                classNotFoundException.printStackTrace();
            } catch (IllegalAccessException illegalAccessException) {
//                illegalAccessException.printStackTrace();
            } catch (InvocationTargetException invocationTargetException) {
//                invocationTargetException.printStackTrace();
            } catch (JsonProcessingException jsonProcessingException) {
//                jsonProcessingException.printStackTrace();
            }
        });

        return result;
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("com.lion.core.common.enums.Delete");
        list.add("com.lion.core.common.enums.State");
        EnumUtil.getAllEnumsInPackage(list).forEach((k, v) -> System.out.println(k + "=" + v));
    }
}
