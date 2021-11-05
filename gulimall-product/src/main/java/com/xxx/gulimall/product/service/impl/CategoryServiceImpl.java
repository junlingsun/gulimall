package com.xxx.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xxx.gulimall.product.dao.CategoryBrandRelationDao;
import com.xxx.gulimall.product.entity.CategoryBrandRelationEntity;
import com.xxx.gulimall.product.vo.Catalog2WebVO;
import com.xxx.gulimall.product.vo.Catalog3WebVO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.common.utils.PageUtils;
import com.xxx.common.utils.Query;

import com.xxx.gulimall.product.dao.CategoryDao;
import com.xxx.gulimall.product.entity.CategoryEntity;
import com.xxx.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationDao relationDao;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;



    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }



    @Override
    public List<CategoryEntity> buildTree() {
        List<CategoryEntity> list = baseMapper.selectList(null);
        List<CategoryEntity> parentList = new ArrayList<>();

        for (CategoryEntity entity: list) {
            if (entity.getParentCid() == 0) parentList.add(entity);
        }
        sort(parentList);

        for (CategoryEntity entity: parentList) {
            treeBuild(entity, list);
        }

        return parentList;
    }

    @Override
    public void deleteByIds(Long[] catIds) {
        baseMapper.deleteBatchIds(Arrays.asList(catIds));
    }

    private void treeBuild(CategoryEntity parentEntity, List<CategoryEntity> list) {
        if(parentEntity == null) return;
        parentEntity.setChildren(new ArrayList<>());
        List<CategoryEntity> parentList = parentEntity.getChildren();


        for (CategoryEntity entity: list) {
            if (entity.getParentCid() == parentEntity.getCatId()) {
//                System.out.println(entity.getCatLevel()+"*****" + entity.getCatId() + );
                parentList.add(entity);
                treeBuild(entity, list);
            }
        }

        sort(parentList);

    }
    private void sort(List<CategoryEntity> list){

        Collections.sort(list, new Comparator<CategoryEntity>(){
            @Override
            public int compare(CategoryEntity o1, CategoryEntity o2) {
                int sort1 = o1.getSort() == null?0:o1.getSort();
                int sort2 = o2.getSort() == null?0:o2.getSort();

                return sort1-sort2;
            }
        });
    }

    @Override
    public List<CategoryEntity> getCategoryList() {
        return baseMapper.selectList(null);
    }

    @Override
    public List<CategoryEntity> getCategoryList(List<Long> catIds) {
        List<CategoryEntity> categoryEntities = baseMapper.selectBatchIds(catIds);
        return categoryEntities;
    }

    @Override
    public List<CategoryEntity> getCategory1List() {
        List<CategoryEntity> list= baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0l));
        return list;
    }

    @Override
    public Map<Long, List<Catalog2WebVO>> getCategory2List(){

        RLock lock = redissonClient.getLock("lock");

        lock.lock();
        try{
            String catalog = redisTemplate.opsForValue().get("catalog");

            if (!StringUtils.hasLength(catalog)) {
                Map<Long, List<Catalog2WebVO>> map = this.getCategory2ListDB();
                redisTemplate.opsForValue().set("catalog", JSON.toJSONString(map), 1, TimeUnit.DAYS);
            }

            return JSON.parseObject(redisTemplate.opsForValue().get("catalog"), new TypeReference<Map<Long, List<Catalog2WebVO>>>(){});

        } finally {
            lock.unlock();
        }


    }



    private Map<Long, List<Catalog2WebVO>> getCategory2ListDB(){
        List<CategoryEntity> categories = this.getCategoryList();

        List<CategoryEntity> level1Categories = categories.stream().filter(entity -> {
            return entity.getParentCid() == 0;
        }).collect(Collectors.toList());

        Map<Long, List<Catalog2WebVO>> map = level1Categories.stream().collect(Collectors.toMap(k -> {
            return k.getCatId();
        }, v -> {
            List<CategoryEntity> categoryEntities = categories.stream().filter(entity -> {
                return entity.getParentCid() == v.getCatId();
            }).collect(Collectors.toList());

            List<Catalog2WebVO> collect = categoryEntities.stream().map(entity -> {
                Catalog2WebVO catalog2WebVO = new Catalog2WebVO();
                catalog2WebVO.setCatalog1Id(entity.getParentCid());
                catalog2WebVO.setId(entity.getCatId());
                catalog2WebVO.setName(entity.getName());

                List<CategoryEntity> categoryEntityList = categories.stream().filter(item -> {
                    return item.getParentCid() == entity.getCatId();
                }).collect(Collectors.toList());


                List<Catalog3WebVO> catalog3WebVOList = categoryEntityList.stream().map(cat -> {
                    Catalog3WebVO catalog3WebVO = new Catalog3WebVO();
                    catalog3WebVO.setCatalog2Id(entity.getCatId());
                    catalog3WebVO.setName(cat.getName());
                    catalog3WebVO.setId(cat.getCatId());
                    return catalog3WebVO;
                }).collect(Collectors.toList());


                catalog2WebVO.setCatalog3List(catalog3WebVOList);
                return catalog2WebVO;
            }).collect(Collectors.toList());

            return collect;
        }));


        return map;
    }

    @Transactional
    @Override
    public void update(CategoryEntity category) {
        this.update(category);
        Long catelogId = category.getCatId();
        String catelogName = category.getName();

        if (StringUtils.hasLength(catelogName)) {
            CategoryBrandRelationEntity relationEntity = new CategoryBrandRelationEntity();
            relationEntity.setCatelogId(catelogId);
            relationEntity.setCatelogName(catelogName);

            relationDao.update(relationEntity, new UpdateWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catelogId));
        }

        //TODO: update other relations
    }
}