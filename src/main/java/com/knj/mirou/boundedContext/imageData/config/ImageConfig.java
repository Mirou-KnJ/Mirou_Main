package com.knj.mirou.boundedContext.imageData.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@RequiredArgsConstructor
public class ImageConfig {

    @Bean
    public ImageAnnotatorSettings visionAPISettings() throws IOException {
        ClassPathResource resource = new ClassPathResource("google-account.json");
        InputStream inputStream = resource.getInputStream();
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(inputStream);
        ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder().setCredentialsProvider(() -> googleCredentials).build();

        return settings;
    }
}
