package com.xxx.gulimall.product.vo;

import lombok.Data;

import java.util.List;

@Data
public class AttrRespVO extends AttrVO{

    private String catelogName;
    private String attrGroupName;

    private List<Long> catelogPath;
}
