package com.knj.mirou.boundedContext.image.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("custom.gcp.auth")
public class GcpAuthConfigProperties {

    private String filePath;

}
