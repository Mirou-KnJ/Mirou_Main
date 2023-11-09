package com.knj.mirou.boundedContext.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.util.IOUtils;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.image.config.S3ConfigProperties;
import com.knj.mirou.boundedContext.image.model.enums.ImageTarget;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageService {

    private final S3ConfigProperties s3ConfigProps;
    private final AmazonS3 amazonS3;
    private final ImageAnnotatorSettings visionAPISettings;
    private final BaseService baseService;

    //TODO: 프로젝트 전체적인 설정이 되도록
    private static String SUCCESS_CODE = "SUCCESS";
    private static String FAIL_CODE = "FAIL";

    public RsData<String> uploadImg(MultipartFile img, ImageTarget imageTarget) throws IOException {

        Map<String, String> extCheckResult = isImgFile(img);

        if(!baseService.checkResultCode(extCheckResult)) {
            return extCheckResult;
        }

//        ObjectMetadata objectMetadata = new ObjectMetadata();
//        objectMetadata.setContentType(img.getContentType());
//        objectMetadata.setContentLength(img.getSize());
//
//        String originFileName = img.getOriginalFilename();
//        int index = originFileName.lastIndexOf(".");
//        String ext = originFileName.substring(index + 1);
//
//        String storeFileName = UUID.randomUUID() + "." + ext;
//
//        //FIXME 이미지 종류에 따라 다른 저장소
////        String storage = switch (imageTarget) {
////            case FEED_IMAGE -> "feed_img";
////        }
//
//        String key = "feed_img/" + storeFileName;
//
//        InputStream inputStream = img.getInputStream();
//
//        String bucket = s3ConfigProps.getBucket();
//        String endPoint = s3ConfigProps.getEndPoint();
//
//        amazonS3.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
//                .withCannedAcl(CannedAccessControlList.PublicRead));
//
//        String result = endPoint + "/%s/%s".formatted(bucket, key);
//
//        detectLabelsGcs(result);
//        safeSearchByGcs(result);

        return resultMap;
    }

    public Map<String, String> isImgFile(MultipartFile img) {

        Map<String, String> resultMap = new HashMap<>();

        String fileExt = img.getOriginalFilename().split("\\.")[1];

        List<String> imgExts = s3ConfigProps.getImgExt();

        if(imgExts.contains(fileExt)) {
            resultMap.put("RS_CODE", SUCCESS_CODE);
            return resultMap;
        }

        resultMap.put("RS_CODE", FAIL_CODE + "이미지 파일이 아닙니다.");
        return resultMap;
    }

    public void detectLabelsGcs(String imgUrl) throws IOException {

        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create(visionAPISettings)) {

            URL url = new URL(imgUrl);
            try (InputStream in = url.openStream()) {
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
                    if (res.hasError()) {
                        //FIXME 에러 문구 처리
                        System.out.println("!!!!에러 발생!!!!");
                        return;
                    }
                    for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {

                        //FIXME 로컬 프린트문 출력 x
                        System.out.println(annotation.getDescription());
                    }
                }
            }
        }
    }

    public void safeSearchByGcs(String imgUrl) throws IOException {

        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create(visionAPISettings)) {

            URL url = new URL(imgUrl);
            try (InputStream in = url.openStream()) {
                byte[] data = IOUtils.toByteArray(in);

                ByteString imgBytes = ByteString.copyFrom(data);
                Image img = Image.newBuilder().setContent(imgBytes).build();
                Feature feat = Feature.newBuilder().setType(Feature.Type.SAFE_SEARCH_DETECTION).build();
                AnnotateImageRequest request =
                        AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();

                BatchAnnotateImagesResponse response = vision.batchAnnotateImages(
                        Collections.singletonList(request));
                List<AnnotateImageResponse> responses = response.getResponsesList();

                for (AnnotateImageResponse res : responses) {
                    if (res.hasError()) {
                        System.out.format("Error: %s%n", res.getError().getMessage());
                        return;
                    }

                    SafeSearchAnnotation safeSearchAnnotation = res.getSafeSearchAnnotation();

                    //유해성 등급 => Unknown, Very Unlikely, Unlikely, Possible, Likely, and Very Likely
                    List<String> resultList = new ArrayList<>();

                    resultList.add(safeSearchAnnotation.getAdult().toString());
                    resultList.add(safeSearchAnnotation.getMedical().toString());
                    resultList.add(safeSearchAnnotation.getAdult().toString());
                    resultList.add(safeSearchAnnotation.getViolence().toString());
                    resultList.add(safeSearchAnnotation.getRacy().toString());

                    for (String result : resultList) {
                        System.out.println("result = " + result);
                    }
                }
            }
        }
        return;
    }

}
