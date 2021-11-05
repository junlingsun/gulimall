package com.xxx.gulimall.ware.service.impl;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.common.utils.PageUtils;
import com.xxx.common.utils.Query;

import com.xxx.gulimall.ware.dao.WareSkuDao;
import com.xxx.gulimall.ware.entity.WareSkuEntity;
import com.xxx.gulimall.ware.service.WareSkuService;
import org.springframework.util.StringUtils;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                new QueryWrapper<WareSkuEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryWareSku(Map<String, Object> params) {
        String skuId = (String)params.get("skuId");
        String wareId = (String)params.get("wareId");
        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();

        if (StringUtils.hasLength(skuId)) {
            wrapper.eq("sku_id", skuId);
        }

        if (StringUtils.hasLength(wareId)) {
            wrapper.eq("ware_id", wareId);
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

    @Override
    public Map<Long, Boolean> stockInfo(List<Long> skuIds) {

        Map<Long, Boolean> map = new HashMap<>();
        for (Long skuId: skuIds) {
            Long stock = baseMapper.getStock(skuId);
            if (stock == null) stock = 0l;
            map.put(skuId, (stock>0));
        }
        return map;
    }
}