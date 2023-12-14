package com.knj.mirou.boundedContext.challengemember.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "custom.challenge")
public class CMemberConfigProperties {

    private int joinLimit;
}
