package com.lion.utils;

import com.lion.exception.BusinessException;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author Mr.Liu
 * @description $
 * @createDateTime 2021/9/2 下午4:10
 */
public class AssertUtil {

    /**
     *
     * @param isTrue
     * @param message
     */
    public static void isTrue(@NotNull Boolean isTrue,@NotBlank String message){
        if (Objects.equals(isTrue,true)){
            BusinessException.throwException(message);
        }
    }

    public static void nonNull(Object obj, @NotBlank String message){
        if (Objects.isNull(obj)){
            BusinessException.throwException(message);
        }
    }

    /**
     *
     * @param isFalse
     * @param message
     */
    public static void isFlase(@NotNull Boolean isFalse,@NotBlank String message) {
        if (Objects.equals(isFalse,false)){
            BusinessException.throwException(message);
        }
    }

    /**
     *
     * @param str
     * @param message
     */
    public static void hasText(@NotBlank String str,@NotBlank String message) {
        if (!StringUtils.hasText(str)){
            BusinessException.throwException(message);
        }
    }
}
