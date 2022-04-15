package com.lion.core.persistence;

import com.lion.core.persistence.entity.BaseEntity;
import com.lion.utils.CurrentUserUtil;
import com.lion.utils.TenantSqlUtil;
import lombok.SneakyThrows;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.select.Select;
import org.hibernate.EmptyInterceptor;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: jpa 多租户 拦截器
 * @author: mr.liu
 * @create: 2020-10-20 16:04
 **/
public class JpaTenantInterceptor extends EmptyInterceptor implements StatementInspector {

    private static final String TENANT_ID ="tenant_id";

    @SneakyThrows
    @Override
    public String inspect( String sql) {
        return TenantSqlUtil.sqlReplace(sql);
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        Long tenantId = CurrentUserUtil.getCurrentUserTenantId();
        BaseEntity baseEntity = (BaseEntity)entity;
        if (Objects.isNull(baseEntity.getId())) {
            baseEntity.setTenantId(Long.valueOf(String.valueOf(tenantId)));
        }
//        return true;
        return super.onSave(entity, id, state, propertyNames, types);
    }
}
