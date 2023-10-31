package com.knj.mirou.boundedContext.image.model.entity;


import com.knj.mirou.boundedContext.image.model.enums.ImageTarget;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long targetId;

    @Enumerated(EnumType.STRING)
    private ImageTarget imageTarget;

    private String imageUrl;

}
