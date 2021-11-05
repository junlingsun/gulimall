package com.xxx.gulimall.product.vo;

import com.xxx.gulimall.product.entity.SkuInfoEntity;
import lombok.Data;

import java.util.List;

@Data
public class SkuVO extends SkuInfoEntity {

    private List<SaleAttrVO> attr;
    private List<SkuImage> images;
    private List<String> descar;
    private Integer fullCount;
    private Double discount;
    private Integer countStatus;
    private Double fullPrice;
    private Integer priceStatus;
    private List<MemberPrice> memberPrice;
}
