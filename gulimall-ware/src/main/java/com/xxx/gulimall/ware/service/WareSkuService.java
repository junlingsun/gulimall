package com.xxx.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.common.utils.PageUtils;
import com.xxx.gulimall.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author Junling Sun
 * @email junlingsun1983@gmail.com
 * @date 2021-09-24 00:08:13
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryWareSku(Map<String, Object> params);

    Map<Long, Boolean> stockInfo(List<Long> skuIds);
}

