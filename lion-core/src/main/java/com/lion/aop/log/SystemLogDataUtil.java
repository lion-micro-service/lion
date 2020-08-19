package com.lion.aop.log;

/**
 * @author mr.liu
 * @title: SystemLogDataUtil
 * @description: 系统日志工具类
 * @date 2020/8/19下午4:38
 */
public class SystemLogDataUtil {
    private static final ThreadLocal<SystemLogData> systemLogDataThreadLocal = new ThreadLocal<SystemLogData>();

    public static SystemLogData get(){
        return systemLogDataThreadLocal.get();
    }

    public static void set(SystemLogData systemLogData){
        systemLogDataThreadLocal.set(systemLogData);
    }

}
