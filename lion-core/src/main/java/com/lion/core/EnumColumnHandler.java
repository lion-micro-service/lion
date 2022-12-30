package com.lion.core;

import org.apache.commons.dbutils.ColumnHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EnumColumnHandler implements ColumnHandler {

    private Class<?> propType;

    @Override
    public boolean match(Class<?> propType) {
        this.propType = propType;
        return IEnum.class.equals(propType);
    }

    @Override
    public Object apply(ResultSet rs, int columnIndex) throws SQLException {
        if (propType.isEnum()) {
            try {
                IEnum iEnum = (IEnum) propType.newInstance();
                return IEnum.instance(rs.getInt(columnIndex));
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
