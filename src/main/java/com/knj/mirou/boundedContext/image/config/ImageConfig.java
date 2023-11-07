package com.knj.mirou.boundedContext.image.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class ImageConfig {

    private final GcpAuthConfigProperties gcpConfigProps;

    @Bean
    public ImageAnnotatorSettings visionAPISettings() throws IOException {
        FileInputStream inputStream = new FileInputStream(gcpConfigProps.getFilePath());
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(inputStream);
        ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder().setCredentialsProvider(() -> googleCredentials).build();

        return settings;
    }
}
