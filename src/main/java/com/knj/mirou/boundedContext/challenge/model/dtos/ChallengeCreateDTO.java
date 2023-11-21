package com.knj.mirou.boundedContext.challenge.model.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class ChallengeCreateDTO {

    @NotBlank
    @Size(min=5, max = 20)
    private String name;

    @NotBlank
    private String contents;

    @NotBlank
    @Max(value = 2000)
    @Min(value = 500)
    private int joinCost;
    
    @NotBlank
    private LocalDate joinDeadLine;

    @NotBlank
    @Min(value = 1)
    @Max(value = 90)
    private int period;

    @NotBlank
    private String tag;

    private String labelList;

    @NotBlank
    private String method;

    @NotBlank
    @Min(value = 1)
    @Max(value = 5)
    private int level;

    @NotBlank
    private String precaution;
}
