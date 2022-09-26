package com.lion.config.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author anders
 * @Description
 * @date 2022/9/23 9:43
 */
@Component // 暂时使用这个注解解决报错，后续改为使用 @EnableConfigurationProperties(AliOssProperties.class)
@ConfigurationProperties(prefix = "ali-oss")
@Data
public class AliOssProperties {

    /**
     * bucket
     */
    private String bucketName;

    /**
     * 访问域名
     */
    private String endpoint;

    /**
     * 访问密钥
     */
    private String accessKeyId;

    /**
     * 秘钥
     */
    private String accessKeySecret;

    /**
     * 路径
     */
    private String path;

}
