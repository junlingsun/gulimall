package com.xxx.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.common.to.SkuReductionTO;
import com.xxx.common.utils.PageUtils;
import com.xxx.gulimall.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * ??Ʒ??????Ϣ
 *
 * @author Junling Sun
 * @email junlingsun1983@gmail.com
 * @date 2021-09-23 23:32:47
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuReduction(SkuReductionTO skuReductionTO);
}

