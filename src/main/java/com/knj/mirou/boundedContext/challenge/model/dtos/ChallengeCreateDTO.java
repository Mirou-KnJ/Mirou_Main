package com.knj.mirou.boundedContext.challenge.model.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChallengeCreateDTO {

    @NotEmpty(message = "제목은 반드시 입력 되어야 합니다.")
    @Size(min = 5, max = 20, message = "제목은 5자 이상 20자 이하로 구성 되어야 합니다.")
    private String name;

    @NotEmpty(message = "내용은 반드시 입력 되어야 합니다.")
    private String contents;

    @NotNull(message = "참가 비용은 반드시 설정 되어야 합니다.")
    @Max(value = 2000)
    @Min(value = 500)
    private int joinCost;

    @NotNull(message = "참여 기한은 반드시 설정 되어야 합니다.")
    private LocalDate joinDeadLine;

    @NotNull(message = "진행 기간은 반드시 설정 되어야 합니다.")
    @Min(value = 1)
    @Max(value = 90)
    private int period;

    @NotEmpty(message = "챌린지 유형 태그는 반드시 설정 되어야 합니다.")
    private String tag;

    private String labelList;

    private String placeCategory;

    @NotEmpty(message = "챌린지 인증 방식은 반드시 설정 되어야 합니다.")
    private String method;

    @NotNull(message = "챌린지 난이도는 반드시 설정 되어야 합니다.")
    @Min(value = 1)
    @Max(value = 5)
    private int level;

    @NotEmpty(message = "주의사항은 반드시 입력 되어야 합니다.")
    private String precaution;
}
