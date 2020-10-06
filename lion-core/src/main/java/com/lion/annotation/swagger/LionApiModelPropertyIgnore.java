package com.lion.annotation.swagger;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * @description: 自定义@ApiModel注解的class要排除哪些Property
 * @author: mr.liu
 * @create: 2020-10-06 15:24
 **/
@Target({TYPE, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface LionApiModelPropertyIgnore {
    /**
     * 定义哪些要排除的属性
     * @return
     */
    String[] propertyIgnore() default {};
}
