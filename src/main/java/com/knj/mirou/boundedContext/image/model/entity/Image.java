package com.knj.mirou.boundedContext.image.model.entity;


import com.knj.mirou.boundedContext.image.model.enums.ImageTarget;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class Image {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    private long targetId;

    @Enumerated(EnumType.STRING)
    private ImageTarget imageTarget;

    private String imageUrl;

}
