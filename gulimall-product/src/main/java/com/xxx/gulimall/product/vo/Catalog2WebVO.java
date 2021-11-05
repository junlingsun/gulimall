package com.xxx.gulimall.product.vo;

import lombok.Data;

import java.util.List;

@Data
public class Catalog2WebVO {
    private Long catalog1Id;
    private Long id;
    private String name;
    private List<Catalog3WebVO> catalog3List;
}
