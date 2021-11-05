package com.xxx.gulimall.elasticsearch.vo;

import com.xxx.common.to.SkuEsTO;
import lombok.Data;

import java.util.List;

@Data
public class SearchResp {

    private List<SkuEsTO> products;

    private Integer pageNum;
    private Long totalRecords;
    private Integer totalPage;

    private List<BrandVO> brands;
    private List<CatalogVO> catalogs;
    private List<AttrVO> attrs;


    @Data
    public static class BrandVO{
        private Long brandId;
        private String brandName;
        private String brandImg;

    }

    @Data
    public static class CatalogVO{
        private Long catalogId;
        private String catalogName;
    }

    @Data
    public static class AttrVO{
        private Long attrId;
        private String attrName;
        private List<String> attrValues;
    }

}
