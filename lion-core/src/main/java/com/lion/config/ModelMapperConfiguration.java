package com.lion.config;

import com.lion.config.properties.MinioProperties;
import io.minio.MinioClient;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mr.Liu
 * @description $
 * @createDateTime 2021/9/3 下午8:50
 */
@Configuration
@ConditionalOnClass({ModelMapper.class})
public class ModelMapperConfiguration {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
