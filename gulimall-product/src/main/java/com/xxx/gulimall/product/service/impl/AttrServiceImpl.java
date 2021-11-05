package com.xxx.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.common.constant.AttrType;
import com.xxx.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.xxx.gulimall.product.dao.AttrGroupDao;
import com.xxx.gulimall.product.dao.CategoryDao;
import com.xxx.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.xxx.gulimall.product.entity.AttrGroupEntity;
import com.xxx.gulimall.product.entity.CategoryEntity;
import com.xxx.gulimall.product.service.AttrAttrgroupRelationService;
import com.xxx.gulimall.product.vo.AttrRespVO;
import com.xxx.gulimall.product.vo.AttrVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.common.utils.PageUtils;
import com.xxx.common.utils.Query;

import com.xxx.gulimall.product.dao.AttrDao;
import com.xxx.gulimall.product.entity.AttrEntity;
import com.xxx.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Autowired
    private AttrAttrgroupRelationDao relationDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private AttrGroupDao attrGroupDao;

    @Autowired
    private AttrDao attrDao;



    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveAttrVo(AttrVO attrVO) {
        //for save, attrId is not available before attrEntity is saved.attrVo doesn't contain attrId
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVO, attrEntity);
        this.save(attrEntity);

        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrId(attrEntity.getAttrId());
        relationEntity.setAttrGroupId(attrVO.getAttrGroupId());

        relationDao.insert(relationEntity);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Integer type, Long catId) {
        log.info("type " + type);

        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_type", type);
        String key = (String)params.get("key");

        if (catId != 0) {
            wrapper.and(obj->{
                obj.eq("catelog_id", catId);
            });
        }

        if (StringUtils.hasLength(key)) {
            wrapper.and(obj->{
                obj.eq("attr_id", key).or().like("attr_name", key);
            });
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);

        List<AttrEntity> records = page.getRecords();
        List<AttrRespVO> respVOS = new ArrayList<>();
        for (AttrEntity attrEntity: records) {
            AttrRespVO respVO = new AttrRespVO();
            BeanUtils.copyProperties(attrEntity, respVO);

            long catgoryId = attrEntity.getCatelogId();
            String catgoryName = categoryDao.selectById(catgoryId).getName();
            respVO.setCatelogName(catgoryName);

            Long attrGroupId = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId())).getAttrGroupId();
            if (attrGroupId != null) {
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
                if (attrGroupEntity != null) {
                    String attrGroupName = attrGroupDao.selectById(attrGroupId).getAttrGroupName();
                    respVO.setAttrGroupName(attrGroupName);
                }


            }


            respVOS.add(respVO);
        }



        pageUtils.setList(respVOS);
        log.info("respVOs " + respVOS);


        return pageUtils;



    }

    @Override
    public AttrRespVO getAttrVOById(Long attrId) {

        AttrEntity entity = this.getById(attrId);
        AttrRespVO respVO = new AttrRespVO();
        BeanUtils.copyProperties(entity, respVO);
        List<CategoryEntity> categoryEntities = categoryDao.selectList(null);
        CategoryEntity categoryEntity = categoryDao.selectById(entity.getCatelogId());
        List<Long> path = new ArrayList<>();
        getPath(categoryEntity, categoryEntities, path);
        Collections.reverse(path);

        respVO.setCatelogPath(path);

        Long attrGroupId = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId)).getAttrGroupId();
        if (attrGroupId != null) {
            respVO.setAttrGroupName(attrGroupDao.selectById(attrGroupId).getAttrGroupName());
            respVO.setAttrGroupId(attrGroupId);
        }

        return respVO;
    }

    private void getPath(CategoryEntity categoryEntity, List<CategoryEntity> categoryEntities, List<Long> path){
        path.add(categoryEntity.getCatId());
        for (CategoryEntity entity: categoryEntities) {
            if (categoryEntity.getParentCid() == entity.getCatId()) {
                getPath(entity, categoryEntities, path);
                break;
            }
        }

    }

    @Transactional
    @Override
    public void updateRespVoById(AttrRespVO attrRespVO) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrRespVO, attrEntity);

        this.updateById(attrEntity);

        long attrId = attrRespVO.getAttrId();
        Long attrGroupId = attrRespVO.getAttrGroupId();
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        if (attrGroupId != null) {
            relationEntity.setAttrGroupId(attrGroupId);
        }

        relationEntity.setAttrId(attrId);
        relationDao.update(relationEntity, new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
    }

    @Override
    public PageUtils getNonAttr(Map<String, Object> param, Long attrGroupId) {
        Long catlogId = attrGroupDao.selectById(attrGroupId).getCatelogId();
        List<AttrGroupEntity> attrGroupEntities = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catlogId));
        List<Long> attrGroupIds = attrGroupEntities.stream().map(attrGroupEntity -> {
            return attrGroupEntity.getAttrGroupId();
        }).collect(Collectors.toList());
        //find all attr that have relations
        List<AttrAttrgroupRelationEntity> relationEntities = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", attrGroupIds));
        List<Long> attrIds = relationEntities.stream().map(relationEntity -> {
            return relationEntity.getAttrId();
        }).collect(Collectors.toList());

        //find all attr that have no relations

        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>();
        queryWrapper.eq("attr_type", AttrType.BASETYPE.getType());

        if (attrIds != null && attrIds.size() != 0) {
            queryWrapper.notIn("attr_id", attrIds);

        }


        String key = (String)param.get("key");

        if (StringUtils.hasLength(key)) {
            queryWrapper.and(wrapper->{
                wrapper.eq("attr_id", key).or().like("attr_name", key);
            });
        }
         IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(param),
                queryWrapper
        );



        return new PageUtils(page);
    }

}