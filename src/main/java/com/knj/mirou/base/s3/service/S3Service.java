package com.knj.mirou.base.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.knj.mirou.boundedContext.image.config.S3ConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3ConfigProperties s3ConfigProps;
    private final AmazonS3 s3Client;

    public InputStream getFileFromS3(String objectKey) throws IOException {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(s3ConfigProps.getBucket())
                    .key(objectKey)
                    .build();

            S3Object s3Object = s3Client.getObject(s3ConfigProps.getBucket(), objectKey);
            return s3Object.getObjectContent();
        } catch (S3Exception e) {
            throw new IOException("S3 파일 읽기 에러");
        }
    }

}
