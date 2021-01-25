package com.lion.config;

import com.fasterxml.classmate.TypeResolver;
import com.lion.core.LionPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.plugin.core.PluginRegistrySupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.readers.operation.OpenApiResponseReader;
import springfox.documentation.swagger2.configuration.Swagger2WebMvcConfiguration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
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
    public Docket docket(ApplicationContext applicationContext){
        List<Response> responses = new ArrayList<Response>();
        Response response = new Response("200","",true, Collections.EMPTY_LIST,Collections.EMPTY_LIST,Collections.EMPTY_LIST,Collections.EMPTY_LIST);
        Docket docket = new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.POST,responses)
                .globalResponses(HttpMethod.GET,responses)
                .globalResponses(HttpMethod.DELETE,responses)
                .globalResponses(HttpMethod.PUT,responses)
                .globalResponses(HttpMethod.PATCH,responses)
                .globalResponses(HttpMethod.OPTIONS,responses)
                .select()
                //设置basePackage会将包下的所有被@Api标记类的所有方法作为api
                .apis(RequestHandlerSelectors.basePackage("com.lion"))
                //只有标记了@ApiOperation的方法才会暴露出给swagger
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
        removeDefaultPlugin(applicationContext);
        return docket;
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

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().build();
    }

    private void removeDefaultPlugin(ApplicationContext applicationContext) {
//        PluginRegistry<OperationBuilderPlugin, DocumentationType> pluginRegistry = applicationContext.getBean("operationBuilderPluginRegistry", PluginRegistry.class);
//        List<OperationBuilderPlugin> plugins = pluginRegistry.getPlugins();
//        OpenApiResponseReader openApiResponseReader = applicationContext.getBean(OpenApiResponseReader.class);
//        if (pluginRegistry.contains(openApiResponseReader)) {
//            List<OperationBuilderPlugin> plugins_new = new ArrayList<OperationBuilderPlugin>(plugins);
//            plugins_new.remove(openApiResponseReader);
//            try {
//                Field field = PluginRegistrySupport.class.getDeclaredField("plugins");
//                field.setAccessible(true);
//                field.set(pluginRegistry, plugins_new);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
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
