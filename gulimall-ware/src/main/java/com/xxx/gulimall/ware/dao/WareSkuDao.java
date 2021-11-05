package com.xxx.gulimall.ware.dao;

import com.xxx.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存
 * 
 * @author Junling Sun
 * @email junlingsun1983@gmail.com
 * @date 2021-09-24 00:08:13
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {


    Long getStock(@Param("skuId") Long skuId);
}
