package com.lion.utils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.select.Select;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mr.Liu
 * @classname TenantSqlUtil
 * @description
 * @date 2022/04/15 下午6:20
 */
public class TenantSqlUtil {

    private static final String WHERE = "tenant_id = ";

    public static String sqlReplace(String sql) throws JSQLParserException {
        Statements statements = CCJSqlParserUtil.parseStatements(sql);
        List<Statement> list = statements.getStatements();
        for (Statement statement : list){
            if (statement instanceof Select) {
                Pattern pattern = Pattern.compile("(tenant_id\\s*\\={1}\\s*\\?{1})");
                Matcher matcher = pattern.matcher(sql);
                if (matcher.find() ) {
                    Long tenantId = CurrentUserUtil.getCurrentUserTenantId();
                    sql = sql.replaceAll("(tenant_id\\s*\\={1}\\s*\\?{1})", WHERE + (Objects.isNull( tenantId)?" null " :tenantId));
                    break;
                }
            }
        }
        return sql;
    }
}
