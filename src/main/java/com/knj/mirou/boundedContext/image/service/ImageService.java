package com.knj.mirou.boundedContext.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.knj.mirou.boundedContext.image.config.S3ConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageService {

    private final S3ConfigProperties s3ConfigProps;
    private final AmazonS3 amazonS3;

    public String uploadImg(MultipartFile img) throws IOException {

        //TODO 이미지 파일이 맞는지 검사

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(img.getContentType());
        objectMetadata.setContentLength(img.getSize());

        String originFileName = img.getOriginalFilename();
        int index = originFileName.lastIndexOf(".");
        String ext = originFileName.substring(index + 1);

        String storeFileName = UUID.randomUUID() + "." + ext;

        //FIXME 이미지 종류에 따라 다른 저장소
//        String storage = switch (imageTarget) {
//            case FEED_IMAGE -> "feed_img";
//        }

        String key = "feed_img/" + storeFileName;

        InputStream inputStream = img.getInputStream();

        String bucket = s3ConfigProps.getBucket();
        String endPoint = s3ConfigProps.getEndPoint();

        amazonS3.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return endPoint + "/%s/%s".formatted(bucket, key);
    }

}
