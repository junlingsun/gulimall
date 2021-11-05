package com.xxx.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.common.utils.PageUtils;
import com.xxx.gulimall.product.entity.CategoryEntity;
import com.xxx.gulimall.product.vo.Catalog2WebVO;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author Junling Sun
 * @email junlingsun1983@gmail.com
 * @date 2021-09-22 23:22:23
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> buildTree();

    void deleteByIds(Long[] catIds);

    List<CategoryEntity> getCategoryList();

    List<CategoryEntity> getCategoryList(List<Long> catIds);

    void update(CategoryEntity category);

    List<CategoryEntity> getCategory1List();

    Map<Long, List<Catalog2WebVO>> getCategory2List();
}

