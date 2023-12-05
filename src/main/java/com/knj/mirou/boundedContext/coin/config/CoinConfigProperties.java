package com.knj.mirou.boundedContext.coin.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "custom.coin")
public class CoinConfigProperties {

    private double quarter;
    private List<Double> standard;
}
