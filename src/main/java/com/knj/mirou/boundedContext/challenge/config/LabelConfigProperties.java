package com.knj.mirou.boundedContext.challenge.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "custom.challenge.label")
public class LabelConfigProperties {

    public List<String> labelingList;
    public List<String> WATER;
    public List<String> BOOKS;
}
