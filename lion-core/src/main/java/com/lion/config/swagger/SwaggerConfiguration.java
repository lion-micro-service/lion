package com.lion.config.swagger;

import com.fasterxml.classmate.TypeResolver;
import com.lion.core.LionPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.AlternateTypeBuilder;
import springfox.documentation.builders.AlternateTypePropertyBuilder;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.swagger2.configuration.Swagger2WebMvcConfiguration;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
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
    @ApiResponses
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
