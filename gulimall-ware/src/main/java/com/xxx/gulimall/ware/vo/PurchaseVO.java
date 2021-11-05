package com.xxx.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseVO {

    private Long purchaseId;
    private List<Long> items;
}
