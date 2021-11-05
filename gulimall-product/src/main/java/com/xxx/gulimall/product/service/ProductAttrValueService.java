package com.xxx.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.common.utils.PageUtils;
import com.xxx.gulimall.product.entity.AttrEntity;
import com.xxx.gulimall.product.entity.ProductAttrValueEntity;
import com.xxx.gulimall.product.vo.BaseAttrVO;

import java.util.List;
import java.util.Map;

/**
 * spu????ֵ
 *
 * @author Junling Sun
 * @email junlingsun1983@gmail.com
 * @date 2021-09-22 23:22:23
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttrs(Long id, List<BaseAttrVO> attrEntities);

    List<ProductAttrValueEntity> getSpuList(Long spuId);
}

