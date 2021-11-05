package com.xxx.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.common.utils.PageUtils;
import com.xxx.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.xxx.gulimall.product.entity.AttrEntity;
import com.xxx.gulimall.product.entity.AttrGroupEntity;
import com.xxx.gulimall.product.vo.AttrGroupVO;
import com.xxx.gulimall.product.vo.AttrVO;

import java.util.List;
import java.util.Map;

/**
 * ???Է??
 *
 * @author Junling Sun
 * @email junlingsun1983@gmail.com
 * @date 2021-09-22 23:22:23
 */


public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, long catId);

    AttrGroupEntity getAttrGroupById(Long attrGroupId);

    List<AttrEntity> getAttr(Long attrGroupId);

    void removeRelationByIds(List<AttrAttrgroupRelationEntity> list);



    List<AttrGroupVO> getAttrGroupVOs(Long catelogId);
}

