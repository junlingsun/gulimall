package com.xxx.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.common.utils.PageUtils;
import com.xxx.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.xxx.gulimall.product.entity.AttrEntity;
import com.xxx.gulimall.product.vo.AttrRespVO;
import com.xxx.gulimall.product.vo.AttrVO;

import java.util.List;
import java.util.Map;

/**
 * ??Ʒ?
 *
 * @author Junling Sun
 * @email junlingsun1983@gmail.com
 * @date 2021-09-22 23:22:23
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttrVo(AttrVO attrVO);

    PageUtils queryPage(Map<String, Object> params, Integer type, Long CatId);


    AttrRespVO getAttrVOById(Long attrId);

    void updateRespVoById(AttrRespVO attrRespVO);

    PageUtils getNonAttr(Map<String, Object> param, Long attrGroupId);

}

