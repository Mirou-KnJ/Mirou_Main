package com.knj.mirou.boundedContext.imageData.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.challenge.model.entity.Challenge;
import com.knj.mirou.boundedContext.challengefeed.model.entity.ChallengeFeed;
import com.knj.mirou.boundedContext.imageData.config.S3ConfigProperties;
import com.knj.mirou.boundedContext.imageData.model.entity.ImageData;
import com.knj.mirou.boundedContext.imageData.model.enums.ImageTarget;
import com.knj.mirou.boundedContext.imageData.model.enums.OptimizerOption;
import com.knj.mirou.boundedContext.imageData.repository.ImageDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ImageDataService {

    private final S3ConfigProperties s3ConfigProps;
    private final AmazonS3 amazonS3;
    private final ImageAnnotatorSettings visionAPISettings;
    private final ImageDataRepository imageDataRepository;

    @Transactional
    public void create(long targetId, ImageTarget imageTarget, String imgUrl) {

        ImageData newImgData = ImageData.builder()
                .targetId(targetId)
                .imageTarget(imageTarget)
                .imageUrl(imgUrl)
                .build();

        imageDataRepository.save(newImgData);
    }

    public ImageData getByIdAndTarget(long targetId, ImageTarget target) {

        Optional<ImageData> OImageData = imageDataRepository.findByTargetIdAndImageTarget(targetId, target);

        if (OImageData.isEmpty()) {
            return null;
        }

        return OImageData.get();
    }

    public RsData<String> tryUploadImg(MultipartFile file, ImageTarget imageTarget) throws IOException {

        RsData<String> isImg = isImgFile(file);

        if (isImgFile(file).isFail()) {
            return isImg;
        }

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        String originFileName = file.getOriginalFilename();
        int index = originFileName.lastIndexOf(".");
        String ext = originFileName.substring(index + 1);

        String storeFileName = UUID.randomUUID() + "." + ext;

        String storage = switch (imageTarget) {
            case FEED_IMG -> "feed_img/";
            case CHALLENGE_IMG -> "challenge_img/";
            case PRODUCT_IMG -> "product_img/";
            case MEMBER_IMG -> "member_img/";
        };

        String key = storage + storeFileName;
        InputStream inputStream = file.getInputStream();
        String bucket = s3ConfigProps.getBucket();
        String endPoint = s3ConfigProps.getEndPoint();

        amazonS3.putObject(new PutObjectRequest(bucket, key, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        String result = endPoint + "/%s/%s".formatted(bucket, key);

        return RsData.of("S-1", "이미지 업로드에 성공하였습니다.", result);
    }

    public RsData<String> isImgFile(MultipartFile file) {

        String fileExt = StringUtils.getFilenameExtension(file.getOriginalFilename());
        List<String> imgExts = s3ConfigProps.getImgExt();

        if (imgExts.contains(fileExt)) {
            return RsData.of("S-1", "이미지 파일입니다.");
        }

        return RsData.of("F-1", "이미지 파일이 아닙니다.");
    }

    public RsData<String> detectLabelsGcs(String imgUrl, List<String> challengeLabels) throws IOException {

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

                boolean isValidImg = false;
                for (AnnotateImageResponse res : responses) {
                    if (res.hasError()) {
                        return RsData.of("F-2", "올바르지 않은 이미지 입니다.");
                    }

                    for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {

                        log.info("검출된 라벨 : " + annotation.getDescription());
                        if (challengeLabels.contains(annotation.getDescription())) {
                            isValidImg = true;
                            break;
                        }
                    }
                }
                if (!isValidImg) {
                    return RsData.of("F-3", "챌린지에 맞지 않는 이미지 입니다.");
                }

                return RsData.of("S-2", "챌린지에 적합한 이미지 입니다.");
            }
        }
    }

    public RsData<String> safeSearchByGcs(String imgUrl) throws IOException {

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
                        return RsData.of("F-4", "올바르지 않은 이미지 입니다.");
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
                        log.info("세이프 서치 결과 : " + result);

                        if (result.equals("LIKELY") || result.equals("VERY LIKELY")) {
                            return RsData.of("F-5", "유해한 이미지일 가능성이 높습니다.");
                        }
                    }
                }
            }
        }
        return RsData.of("S-3", "유해하지 않은 이미지 입니다.");
    }

    public String getOptimizingUrl(String imgUrl, OptimizerOption option) {

        String[] split = imgUrl.split(s3ConfigProps.getBucket());
        String cdnUrl = s3ConfigProps.getCdnUrl();

        StringBuilder sb = new StringBuilder();
        sb.append(cdnUrl);
        sb.append(split[1]);
        sb.append(option.getOption());

        return sb.toString();
    }

    public Map<Long, String> getListImages(List<Challenge> challenges) {

        Map<Long, String> images = new HashMap<>();

        for (Challenge challenge : challenges) {
            String optimizedUrl = getOptimizingUrl(challenge.getImgUrl(), OptimizerOption.CHALLENGE_LIST);
            images.put(challenge.getId(), optimizedUrl);
        }

        return images;
    }

    public Map<Long, String> getMyFeedListImages(List<ChallengeFeed> challengeFeeds) {

        Map<Long, String> images = new HashMap<>();

        for (ChallengeFeed feed : challengeFeeds) {
            String optimizedUrl = getOptimizingUrl(feed.getImgUrl(), OptimizerOption.HISTORY);
            images.put(feed.getId(), optimizedUrl);
        }

        return images;
    }

    public Map<Long, String> getNotMineFeedListImages(List<ChallengeFeed> challengeFeeds) {

        Map<Long, String> images = new HashMap<>();

        for (ChallengeFeed feed : challengeFeeds) {
            String optimizedUrl = getOptimizingUrl(feed.getImgUrl(), OptimizerOption.FEED_MODAL);
            images.put(feed.getId(), optimizedUrl);
        }

        return images;
    }
}
