package com.lion.config.swagger;

import com.lion.annotation.swagger.LionApiResponses;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ResponseBuilderPlugin;
import springfox.documentation.spi.service.contexts.ResponseContext;
import springfox.documentation.swagger2.configuration.Swagger2WebMvcConfiguration;

import java.util.Optional;

/**
 * @description: 自定义swagger注解（swagger插件）
 * @author: mr.liu
 * @create: 2020-10-07 07:42
 **/
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
@ConditionalOnClass({ResponseBuilderPlugin.class, Swagger2WebMvcConfiguration.class})
public class LionApiResponsesReader implements ResponseBuilderPlugin {

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }


    @Override
    public void apply(ResponseContext responseContext) {
        Optional<LionApiResponses> annotation = responseContext.getOperationContext().findAnnotation(LionApiResponses.class);
        if (annotation.isPresent()){
            ResponseBuilder responseBuilder = responseContext.responseBuilder();
        }
    }
}
