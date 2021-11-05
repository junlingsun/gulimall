package com.xxx.gulimall.product.service.impl;

import com.xxx.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.xxx.gulimall.product.dao.AttrDao;
import com.xxx.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.xxx.gulimall.product.entity.AttrEntity;
import com.xxx.gulimall.product.entity.CategoryEntity;
import com.xxx.gulimall.product.service.CategoryService;
import com.xxx.gulimall.product.vo.AttrGroupVO;
import com.xxx.gulimall.product.vo.AttrVO;
import feign.QueryMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.common.utils.PageUtils;
import com.xxx.common.utils.Query;

import com.xxx.gulimall.product.dao.AttrGroupDao;
import com.xxx.gulimall.product.entity.AttrGroupEntity;
import com.xxx.gulimall.product.service.AttrGroupService;
import org.springframework.util.StringUtils;

@Slf4j
@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrAttrgroupRelationDao relationDao;

    @Autowired
    private AttrDao attrDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, long catId) {
        log.info("catId " + catId);
        String key = (String)params.get("key");

        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();



        if (StringUtils.hasLength(key)) {
            wrapper.eq("attr_group_id", key).or().like("attr_group_name", key).or().like("descript", key);
            if (catId == 0) {
                IPage<AttrGroupEntity> page = this.page(
                        new Query<AttrGroupEntity>().getPage(params),
                        wrapper
                );

                return new PageUtils(page);
            }else {
                wrapper.and((obj)->{
                    obj.eq("catelog_id", catId);
                });
                IPage<AttrGroupEntity> page = this.page(
                        new Query<AttrGroupEntity>().getPage(params),
                        wrapper
                );

                return new PageUtils(page);
            }

        }else {
            if (catId == 0) {
                return this.queryPage(params);
            }else {
                wrapper.eq("catelog_id", catId);
                IPage<AttrGroupEntity> page = this.page(
                        new Query<AttrGroupEntity>().getPage(params),
                        wrapper
                );


                return new PageUtils(page);
            }

        }
    }

    @Override
    public AttrGroupEntity getAttrGroupById(Long attrGroupId) {
        AttrGroupEntity entity = this.getById(attrGroupId);
        long catId = entity.getCatelogId();

        List<CategoryEntity> categoryEntities = categoryService.getCategoryList();
        CategoryEntity categoryEntity = categoryService.getById(catId);
        List<Long> path = new ArrayList<>();
        getPath(categoryEntity, categoryEntities, path);
        Collections.reverse(path);
        entity.setCatelogPath(path);
        return entity;
    }

    private void getPath(CategoryEntity categoryEntity, List<CategoryEntity> categoryEntities, List<Long> path) {
        Long catId = categoryEntity.getCatId();
        path.add(catId);

        for (CategoryEntity entity: categoryEntities) {
            if (entity.getCatId() == categoryEntity.getParentCid()) {
                getPath(entity, categoryEntities, path);
            }
        }
    }

    @Override
    public List<AttrEntity> getAttr(Long attrGroupId) {

        QueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupId);



        List<AttrAttrgroupRelationEntity> relationEntities = relationDao.selectList(queryWrapper);
        if (relationEntities == null || relationEntities.isEmpty()) return null;

        List<Long> attrIds = new ArrayList<>();
        for (AttrAttrgroupRelationEntity relationEntity: relationEntities) {
            attrIds.add(relationEntity.getAttrId());
        }


        List<AttrEntity> attrEntities = attrDao.selectBatchIds(attrIds);

        return attrEntities;
    }

    @Override
    public void removeRelationByIds(List<AttrAttrgroupRelationEntity> list) {
        relationDao.deleteBatch(list);

    }

    @Override
    public List<AttrGroupVO> getAttrGroupVOs(Long catelogId) {

        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("catelog_id", catelogId);
        List<AttrGroupEntity> attrGroupEntities = baseMapper.selectList(wrapper);

        List<AttrGroupVO> attrGroupVOS = new ArrayList<>();
        for (AttrGroupEntity attrGroupEntity: attrGroupEntities) {
            AttrGroupVO attrGroupVO = new AttrGroupVO();
            BeanUtils.copyProperties(attrGroupEntity, attrGroupVO);


            List<AttrAttrgroupRelationEntity> relationEntities = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>()
            .eq("attr_group_id", attrGroupEntity.getAttrGroupId()));

            List<Long> attrIds = relationEntities.stream().map(relationEntity -> {
                return relationEntity.getAttrId();
            }).collect(Collectors.toList());

            List<AttrEntity> attrEntities = attrDao.selectBatchIds(attrIds);
            attrGroupVO.setAttrs(attrEntities);
            attrGroupVOS.add(attrGroupVO);
        }
        return attrGroupVOS;
    }
}