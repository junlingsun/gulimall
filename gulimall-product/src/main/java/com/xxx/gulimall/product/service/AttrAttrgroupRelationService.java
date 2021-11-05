package com.xxx.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.common.utils.PageUtils;
import com.xxx.gulimall.product.entity.AttrAttrgroupRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * ????&???Է???????
 *
 * @author Junling Sun
 * @email junlingsun1983@gmail.com
 * @date 2021-09-22 23:22:23
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);


    void addRelation(List<AttrAttrgroupRelationEntity> relationEntities);
}

