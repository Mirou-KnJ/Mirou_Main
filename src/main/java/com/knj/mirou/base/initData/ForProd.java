package com.knj.mirou.base.initData;

import com.knj.mirou.boundedContext.challenge.config.LabelConfig;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Profile({"prod"})
public class ForProd {

    @Bean
    CommandLineRunner initData(
            LabelConfig labelConfig
    ) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {

                labelConfig.setLabels();

            }
        };
    }
}
