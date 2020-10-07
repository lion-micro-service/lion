package com.lion.config.swagger;

import com.fasterxml.classmate.TypeResolver;
import com.lion.core.LionPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationModelsProviderPlugin;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.configuration.Swagger2WebMvcConfiguration;

import java.util.ArrayList;
import java.util.List;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

/**
 * @description:
 * @author: mr.liu
 * @create: 2020-10-06 10:39
 **/
@Configuration
@ConditionalOnClass({Swagger2WebMvcConfiguration.class})
@ConditionalOnMissingClass({"org.springframework.cloud.gateway.config.GatewayAutoConfiguration"})
@ConditionalOnWebApplication
public class SwaggerConfiguration {



    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.OAS_30)
                .select()
                //设置basePackage会将包下的所有被@Api标记类的所有方法作为api
                .apis(RequestHandlerSelectors.basePackage("com.lion"))
                //只有标记了@ApiOperation的方法才会暴露出给swagger
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any()).build();
    }

    @Bean
    public AlternateTypeRuleConvention pageableConvention(final TypeResolver resolver) {
        return new AlternateTypeRuleConvention() {
            @Override
            public int getOrder() {
                return Ordered.LOWEST_PRECEDENCE;
            }
            @Override
            public List<AlternateTypeRule> rules() {
                List<AlternateTypeRule> list = new ArrayList<AlternateTypeRule>();
                list.add(newRule(resolver.resolve(LionPage.class), resolver.resolve(Page.class)));
                return list;
            }
        };
    }

    @ApiModel
    @Data
    static class Page {
        @ApiModelProperty("第几页,从0开始计数")
        private Integer pageNumber;

        @ApiModelProperty("每页数据数量")
        private Integer pageSize;
    }
}
