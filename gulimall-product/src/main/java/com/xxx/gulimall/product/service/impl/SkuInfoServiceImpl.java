package com.xxx.gulimall.product.service.impl;

import com.xxx.gulimall.product.vo.SaleAttrVO;
import com.xxx.gulimall.product.vo.SkuSaleAttrVO;
import com.xxx.gulimall.product.vo.SkuVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.common.utils.PageUtils;
import com.xxx.common.utils.Query;

import com.xxx.gulimall.product.dao.SkuInfoDao;
import com.xxx.gulimall.product.entity.SkuInfoEntity;
import com.xxx.gulimall.product.service.SkuInfoService;
import org.springframework.util.StringUtils;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Autowired
    private SkuInfoDao skuInfoDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils querySkuInfo(Map<String, Object> params) {
        String catelogId = (String)params.get("catelogId");
        String brandId = (String)params.get("brandId");
        String key = (String)params.get("key");
        String min = (String)params.get("min");
        String max = (String)params.get("max");

        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();
        if (StringUtils.hasLength(catelogId) && !catelogId.equals("0")) {
            wrapper.eq("catalog_id", catelogId);
        }

        if (StringUtils.hasLength(brandId) && !brandId.equals("0")) {
            wrapper.eq("brand_id", brandId);
        }

        if (StringUtils.hasLength(min)) {
            wrapper.gt("price", min);
        }

        if (StringUtils.hasLength(max)) {
            wrapper.le("price", max);
        }

        if (StringUtils.hasLength(key)) {
            wrapper.and(obj ->{
                obj.eq("sku_id", key).or().eq("spu_id", key).or().like("sku_name", key)
                        .or().like("sku_desc", key);
            });
        }

        IPage page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }
}