package com.lion.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.xa.DataSourceProxyXA;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * @description:
 * @author: mr.liu
 * @create: 2020-10-12 19:40
 **/
@Configuration
@ConditionalOnClass( {DataSourceProxyXA.class} )
public class SeataXaDataSource {

//    @ConfigurationProperties(prefix = "spring.datasource")
//    @Bean("druidDataSource")
//    public DataSource druidDataSource() {
//        return new DruidDataSource();
//    }
//
//    @Bean("dataSource")
//    @Primary
//    public DataSourceProxyXA dataSource(DataSource druidDataSource) {
//        return new DataSourceProxyXA(druidDataSource);
//    }
}
