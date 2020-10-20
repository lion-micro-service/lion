package com.lion.core.persistence;

import org.hibernate.resource.jdbc.spi.StatementInspector;

/**
 * @description: jpa sql 拦截器
 * @author: mr.liu
 * @create: 2020-10-20 16:04
 **/
public class JpaInterceptor implements StatementInspector {
    @Override
    public String inspect(String sql) {
        return sql;
    }
}
