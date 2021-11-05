package com.xxx.common.to;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuEsTO {

    private Long skuId;
    private Long spuId;
    private String skuName;
    private BigDecimal price;
    private String skuDefaultImage;
    private Long saleCount;
    private Boolean hasStock;
    private Long hotScore;
    private Long brandId;
    private Long catalogId;
    private String brandName;
    private String brandImg;
    private String catalogName;
    private List<AttrTO> attrs;


}
