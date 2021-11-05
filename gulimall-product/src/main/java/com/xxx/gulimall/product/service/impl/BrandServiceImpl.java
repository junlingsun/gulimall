package com.xxx.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xxx.gulimall.product.dao.CategoryBrandRelationDao;
import com.xxx.gulimall.product.entity.CategoryBrandRelationEntity;
import com.xxx.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.common.utils.PageUtils;
import com.xxx.common.utils.Query;

import com.xxx.gulimall.product.dao.BrandDao;
import com.xxx.gulimall.product.entity.BrandEntity;
import com.xxx.gulimall.product.service.BrandService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Autowired
    private CategoryBrandRelationDao relationDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        String key = (String)params.get("key");
        if (!StringUtils.hasLength(key)) {
            IPage<BrandEntity> page = this.page(
                    new Query<BrandEntity>().getPage(params),
                    new QueryWrapper<BrandEntity>()
            );

            return new PageUtils(page);
        }
        QueryWrapper<BrandEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("brand_id", key).or().like("name", key).or().like("descript", key);
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);


    }

    @Transactional
    @Override
    public void update(BrandEntity brand) {
        this.updateById(brand);
        Long brandId = brand.getBrandId();
        String brandName = brand.getName();
        if (StringUtils.hasLength(brandName)) {
            CategoryBrandRelationEntity relationEntity = new CategoryBrandRelationEntity();
            relationEntity.setBrandId(brandId);
            relationEntity.setBrandName(brandName);
            relationDao.update(relationEntity, new UpdateWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId));

            //TODO: update other relations
        }

    }


}