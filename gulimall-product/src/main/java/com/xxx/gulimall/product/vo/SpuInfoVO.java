package com.xxx.gulimall.product.vo;

import com.xxx.gulimall.product.entity.AttrEntity;
import com.xxx.gulimall.product.entity.SkuInfoEntity;
import com.xxx.gulimall.product.entity.SpuInfoEntity;
import lombok.Data;

import java.util.List;

@Data
public class SpuInfoVO extends SpuInfoEntity {

    private List<String> decript;
    private List<String> images;
    private Bounds bounds;

    private List<BaseAttrVO> baseAttrs;
    private List<SkuVO> skus;




}



