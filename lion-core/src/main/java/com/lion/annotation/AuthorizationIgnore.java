package com.lion.annotation;

import java.lang.annotation.*;

/**
 * 排除授权认证
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthorizationIgnore {
}
