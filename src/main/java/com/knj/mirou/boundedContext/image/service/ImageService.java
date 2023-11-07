package com.knj.mirou.boundedContext.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import com.knj.mirou.base.s3.service.S3Service;
import com.knj.mirou.boundedContext.image.config.S3ConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageService {

    private final S3ConfigProperties s3ConfigProps;
    private final AmazonS3 amazonS3;
    private final S3Service s3Service;
    private final ImageAnnotatorSettings visionAPISettings;

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

        String result = endPoint + "/%s/%s".formatted(bucket, key);

        detectLabelsGcs(result);

        return endPoint + "/%s/%s".formatted(bucket, key);
    }

    public void detectLabelsGcs(String imgUrl) throws IOException {

        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create(visionAPISettings)) {

            URL url = new URL(imgUrl);

            try(InputStream in = url.openStream()) {
                byte[] data = IOUtils.toByteArray(in);

                ByteString imgBytes = ByteString.copyFrom(data);
                Image img = Image.newBuilder().setContent(imgBytes).build();
                Feature feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();

                AnnotateImageRequest request =
                        AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();

                BatchAnnotateImagesResponse response = vision.batchAnnotateImages(
                        Collections.singletonList(request));

                List<AnnotateImageResponse> responses = response.getResponsesList();

                for (AnnotateImageResponse res : responses) {

                    if(res.hasError()) {
                        //FIXME 에러 문구 처리
                        System.out.println("!!!!에러 발생!!!!");
                        return;
                    }

                    for(EntityAnnotation annotation : res.getLabelAnnotationsList()) {

                        //FIXME 로컬 프린트문 출력 x
                        System.out.println(annotation.getDescription());

                    }
                }
            }
        }
    }

}
