package com.knj.mirou.boundedContext.challenge.model.dtos;

import com.knj.mirou.boundedContext.challenge.model.enums.AuthenticationMethod;
import com.knj.mirou.boundedContext.challenge.model.enums.ChallengeTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ChallengeCreateDTO {

    private String name;

    private String contents;

    private int joinCost;

    private LocalDate joinDeadLine;

    private int period;

    private ChallengeTag challengeTag;

    private AuthenticationMethod method;

    private int level;

    private String precaution;

    private MultipartFile img;

}
