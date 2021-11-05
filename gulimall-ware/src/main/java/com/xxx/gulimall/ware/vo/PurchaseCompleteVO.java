package com.xxx.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseCompleteVO {
    private Long id;
    private List<PurchaseDetailVO> items;
}
