package com.knj.mirou.boundedContext.challenge.config;

import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeLabel;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeTag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class LabelConfig {

    private final LabelConfigProperties labelConfigProps;

    @Bean
    public void setLabels() {

        Class<?> configClass = LabelConfigProperties.class;

        List<String> labelingTargets = labelConfigProps.getLabelingList();

        for(String target : labelingTargets) {
            try {
                Field field = configClass.getDeclaredField(target);
                field.setAccessible(true);

                List<String> targetList = (List<String>) field.get(labelConfigProps);

                ChallengeLabel.valueOf(target).setLabels(targetList);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
