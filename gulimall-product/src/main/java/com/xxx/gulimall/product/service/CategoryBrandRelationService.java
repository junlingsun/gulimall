package com.xxx.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.common.utils.PageUtils;
import com.xxx.gulimall.product.entity.BrandEntity;
import com.xxx.gulimall.product.entity.CategoryBrandRelationEntity;
import com.xxx.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * Ʒ?Ʒ???????
 *
 * @author Junling Sun
 * @email junlingsun1983@gmail.com
 * @date 2021-09-22 23:22:23
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryBrandRelationEntity> listByBrandId(long brandId);

    void save(long brandId, long catelogId);

    List<BrandEntity> getBrandList(Long catId);
}

