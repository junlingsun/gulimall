package com.xxx.gulimall.elasticsearch.vo;

import lombok.Data;

import java.util.List;

@Data
public class SearchParam {


    private Long catalogId;
    private String keyword;


    private String sort;
    private Integer hasStock;
    private String skuPrice;
    private List<Long> brandId;
    private List<String> attrs;
    private Integer pageNum = 1;
}
