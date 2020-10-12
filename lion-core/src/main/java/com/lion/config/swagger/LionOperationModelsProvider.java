package com.lion.config.swagger;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.types.ResolvedObjectType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lion.annotation.swagger.LionApiResponses;
import com.lion.core.IResultData;
import com.lion.core.ResultData;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationModelsProviderPlugin;
import springfox.documentation.spi.service.ResponseBuilderPlugin;
import springfox.documentation.spi.service.contexts.RequestMappingContext;
import springfox.documentation.swagger2.configuration.Swagger2WebMvcConfiguration;

import java.util.Optional;

/**
 * @description:
 * @author: mr.liu
 * @create: 2020-10-07 09:15
 **/
@Component
@ConditionalOnClass({OperationModelsProviderPlugin.class, Swagger2WebMvcConfiguration.class})
public class LionOperationModelsProvider implements OperationModelsProviderPlugin {
    @SneakyThrows
    @Override
    public void apply(RequestMappingContext context) {
        Optional<LionApiResponses> lionApiResponses = context.findAnnotation(LionApiResponses.class);
        if (lionApiResponses.isPresent()){

        }

    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}
