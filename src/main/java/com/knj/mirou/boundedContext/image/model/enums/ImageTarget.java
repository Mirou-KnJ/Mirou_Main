package com.knj.mirou.boundedContext.image.model.enums;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@RequiredArgsConstructor
public enum ImageTarget {

    FEED_IMG("피드 이미지"),
    CHALLENGE_IMG("챌린지 이미지"),
    PRODUCT_IMG("상품 이미지"),
    MEMBER_IMG("회원 이미지");

    private final String imageTarget;

}
