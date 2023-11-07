package com.knj.mirou.boundedContext.image.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import com.knj.mirou.base.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@RequiredArgsConstructor
public class ImageConfig {

    private final GcpAuthConfigProperties gcpConfigProps;
    private final S3Service s3Service;

    @Bean
    public ImageAnnotatorSettings visionAPISettings() throws IOException {
        FileInputStream inputStream = new FileInputStream(gcpConfigProps.getFilePath());
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(inputStream);
        ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder().setCredentialsProvider(() -> googleCredentials).build();

        return settings;
    }
}
