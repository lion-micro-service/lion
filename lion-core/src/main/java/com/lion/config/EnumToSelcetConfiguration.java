package com.lion.config;

import com.lion.core.IResultData;
import com.lion.core.LionObjectMapper;
import com.lion.utils.EnumUtil;
import lombok.Data;
import org.aspectj.lang.annotation.After;
import org.reflections.Reflections;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author mr.liu
 * @Description:
 * @date 2020/9/16上午9:26
 */
@Component
@ConditionalOnWebApplication
@ConditionalOnClass({Reflections.class})
@Order(Ordered.LOWEST_PRECEDENCE)
@ConditionalOnExpression("!'${lion.enums}'.isEmpty()")
@ConfigurationProperties(prefix = "lion")
@Data
public class EnumToSelcetConfiguration implements CommandLineRunner {

    @Value("${lion.enums.url:http://lion-common-console-restful/}")
    private String LB_URL ;

    private List enums;

    @Autowired
    @Qualifier(RestTemplateConfiguration.REST_TAMPLATE_LOAD_BALANCED_BEAN_NAME)
    private RestTemplate restTemplate;

    @Override
    public void run(String... args) {
        try {
            Map<String, Object> map = EnumUtil.getAllEnumsInPackage("com.lion");
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            map.forEach((k, v) ->{
                Map<String, String> temp = new HashMap<String, String>();
                temp.put("classs",k);
                temp.put("value",v.toString());
                list.add(temp);
            });
            if (list.size()<=0){
                return;
            }
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
            LionObjectMapper objectMapper = new LionObjectMapper();
            HttpEntity<String> request = new HttpEntity<String>(objectMapper.writeValueAsString(list),headers);
            ResponseEntity response = restTemplate.postForEntity(LB_URL+"/common/enum/console/persistence", request, Object.class);
        }catch (Exception exception){

        }

    }
}
