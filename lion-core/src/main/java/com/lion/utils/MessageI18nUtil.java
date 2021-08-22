package com.lion.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class MessageI18nUtil {

    private static MessageSource messageSource;

    public MessageI18nUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public static String getMessage(String code){
        return getMessage(code, null);
    }

    public static String getMessage(String code, Object[] args){
        return messageSource.getMessage(code,args, LocaleContextHolder.getLocale());
    }
}
