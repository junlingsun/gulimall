package com.xxx.gulimall.product.service.impl;

import com.xxx.gulimall.product.dao.BrandDao;
import com.xxx.gulimall.product.entity.BrandEntity;
import com.xxx.gulimall.product.entity.CategoryEntity;
import com.xxx.gulimall.product.service.BrandService;
import com.xxx.gulimall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.common.utils.PageUtils;
import com.xxx.common.utils.Query;

import com.xxx.gulimall.product.dao.CategoryBrandRelationDao;
import com.xxx.gulimall.product.entity.CategoryBrandRelationEntity;
import com.xxx.gulimall.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private BrandDao brandDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryBrandRelationEntity> listByBrandId(long brandId) {
        QueryWrapper<CategoryBrandRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("brand_id", brandId);
        List<CategoryBrandRelationEntity> relationEntities = baseMapper.selectList(wrapper);
        return relationEntities;
    }

    @Override
    public void save(long brandId, long catelogId) {

        CategoryEntity categoryEntity = categoryService.getById(catelogId);
        BrandEntity brandEntity = brandService.getById(brandId);

        CategoryBrandRelationEntity relationEntity = new CategoryBrandRelationEntity();
        relationEntity.setBrandId(brandId);
        relationEntity.setCatelogId(catelogId);
        relationEntity.setBrandName(brandEntity.getName());
        relationEntity.setCatelogName(categoryEntity.getName());

        this.save(relationEntity);
    }

    @Override
    public List<BrandEntity> getBrandList(Long catId) {
        QueryWrapper<CategoryBrandRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("catelog_id", catId);
        List<CategoryBrandRelationEntity> relationEntities = baseMapper.selectList(wrapper);
        if(relationEntities == null || relationEntities.isEmpty()) return null;
        List<Long> brandIds = relationEntities.stream().map(relationEntity->{
            return relationEntity.getBrandId();
        }).collect(Collectors.toList());

        List<BrandEntity> brandEntities = brandDao.selectBatchIds(brandIds);

        return brandEntities;
    }
}