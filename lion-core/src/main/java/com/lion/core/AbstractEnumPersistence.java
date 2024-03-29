package com.lion.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lion.config.RestTemplateConfiguration;
import com.lion.utils.EnumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author mr.liu
 * @Description:
 * @date 2020/9/16上午9:26
 */

public abstract class AbstractEnumPersistence implements CommandLineRunner {

    @Value("${lion.enums.persistence.url:http://lion-common-console-restful}")
    private String LB_URL ;

    @Autowired
    @Qualifier(RestTemplateConfiguration.REST_TAMPLATE_LOAD_BALANCED_BEAN_NAME)
    private RestTemplate restTemplate;

    @Value("${spring.application.name:''}")
    private String springApplicationName;

    private String packageName;

    @Autowired
    private ObjectMapper objectMapper;

    public AbstractEnumPersistence(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public void run(String... args) {
        try {
            Map<String, Object> map = EnumUtil.getAllEnumsInPackage(this.packageName);
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            map.forEach((k, v) ->{
                Map<String, String> temp = new HashMap<String, String>();
                temp.put("classs",k);
                temp.put("value",String.valueOf(v));
                list.add(temp);
            });
            if (list.size()<=0){
                return;
            }
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<String> request = new HttpEntity<String>(objectMapper.writeValueAsString(list),headers);
            if (Objects.equals(springApplicationName,"lion-common-console-restful")) {
                Thread.sleep(1000);
            }
            ResponseEntity response = restTemplate.postForEntity(LB_URL+"/enum/console/persistence", request, Object.class);
        }catch (Exception exception){
            exception.printStackTrace();
        }

    }
}
