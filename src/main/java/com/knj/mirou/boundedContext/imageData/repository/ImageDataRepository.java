package com.knj.mirou.boundedContext.imageData.repository;

import com.knj.mirou.boundedContext.imageData.model.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageDataRepository extends JpaRepository<ImageData, Long> {
}
