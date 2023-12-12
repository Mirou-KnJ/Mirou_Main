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
    public List<String> MUSIC;
    public List<String> VITAMIN;
    public List<String> MORNING;
    public List<String> BRUSH;
    public List<String> PLAN;
    public List<String> RECEIPT;
    public List<String> WEATHER;
    public List<String> TEMPERATURE;
    public List<String> PLANT;
    public List<String> MOVIE;
}
