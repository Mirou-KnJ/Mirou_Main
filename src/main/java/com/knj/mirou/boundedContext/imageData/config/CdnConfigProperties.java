package com.knj.mirou.boundedContext.imageData.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("custom.img.aws.cdn")
public class CdnConfigProperties {

    private String url;
    private String challengeDetail;

}
