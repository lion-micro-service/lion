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
import java.util.stream.Collectors;

/**
 * @author Mr.Liu
 * @classname TenantSqlUtil
 * @description
 * @date 2022/04/15 下午6:20
 */
public class TenantSqlUtil {

    private static final String WHERE = "tenant_id = ";

    private static final String PATTERN_TENANT = "((tenant_id){1}\\s*\\={1}\\s*\\?{1})";

    private static final String PATTERN_WHERE = "((and)*\\s*1\\s*\\={1}\\s*1)";

    private static final String PATTERN_WHERE1 = "((where)\\s*1\\s*\\={1}\\s*1\\s*(and))";

    private static final String PATTERN_WHERE2 = "((create_user_id)\\s*in{1}\\s*(#userIds#))";

    public static String sqlReplace(String sql) throws JSQLParserException {
        Statements statements = CCJSqlParserUtil.parseStatements(sql);
        List<Statement> list = statements.getStatements();
        for (Statement statement : list){
            if (statement instanceof Select) {
                Pattern pattern = Pattern.compile(PATTERN_TENANT);
                Matcher matcher = pattern.matcher(sql);
                if (matcher.find() ) {
                    Long tenantId = CurrentUserUtil.getCurrentUserTenantId();
                    sql = sql.replaceAll(PATTERN_TENANT, WHERE + (Objects.isNull( tenantId)?" null " :tenantId));
                }

                pattern = Pattern.compile(PATTERN_WHERE1);
                matcher = pattern.matcher(sql);
                if (matcher.find() ) {
                    sql = sql.replaceAll(PATTERN_WHERE1, " where ");
                }

                pattern = Pattern.compile(PATTERN_WHERE);
                matcher = pattern.matcher(sql);
                if (matcher.find() ) {
                    sql = sql.replaceAll(PATTERN_WHERE, " ");
                }

                pattern = Pattern.compile(PATTERN_WHERE2);
                matcher = pattern.matcher(sql);
                if (matcher.find() ) {
                    List<Long> list1 = CurrentUserUtil.getDataAuthority();
                    if (list1.size()>0) {
                        sql = sql.replaceAll(PATTERN_WHERE2, "create_user_id in ("+ list1.stream().map(String::valueOf).collect(Collectors.joining(","))+") ");
                    }else {
                        sql = sql.replaceAll("(\\s*(\\w)*(.){1}(create_user_id)\\s*in{1}\\s*(#userIds#))", " ");
                    }
                }

                break;
            }
        }
        return sql;
    }



    public static void main(String agrs[]) throws JSQLParserException {
        String sql = "select * from t_user where  1=1 and tenant_id=? and 1=1 ";
        System.out.println(TenantSqlUtil.sqlReplace(sql));
    }
}
