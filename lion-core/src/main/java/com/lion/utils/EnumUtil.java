package com.lion.utils;

import org.reflections.Reflections;

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
            Map<String, Object> value = new HashMap<String, Object>();
            try {
                Class t = Class.forName(e);
                Arrays.asList(t.getEnumConstants()).forEach(c -> {
                    Map<String, Object> fieldValue = new HashMap<String, Object>();
                    Arrays.asList(c.getClass().getDeclaredFields()).stream()
                            .filter(field -> (!field.isEnumConstant()) && !"$VALUES".equals(field.getName()))
                            .forEach(field -> {
                                field.setAccessible(true);
                                try {
                                    fieldValue.put(field.getName(), field.get(c));
                                } catch (IllegalAccessException illegalAccessException) {
                                    illegalAccessException.printStackTrace();
                                }
                            });
                    value.put(c.toString(), fieldValue);
                });
                result.put(t.getCanonicalName(), value);
            }
            catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
        });

        return result;
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("com.lion.core.common.enums.Delete");
        EnumUtil.getAllEnumsInPackage(list).forEach((k, v) -> System.out.println(k + "=" + v));
    }
}
