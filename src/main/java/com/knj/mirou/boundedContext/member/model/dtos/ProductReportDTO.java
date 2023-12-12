package com.knj.mirou.boundedContext.member.model.dtos;

import com.knj.mirou.boundedContext.product.model.entity.ProductInfo;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@RequiredArgsConstructor
public class ProductReportDTO {

    public ProductInfo weeklyBestProduct;
    public int weeklyBestProductSalesCount;
    public int weeklyAllSalesCount;
    public int weeklyAllUsedCount;
}
