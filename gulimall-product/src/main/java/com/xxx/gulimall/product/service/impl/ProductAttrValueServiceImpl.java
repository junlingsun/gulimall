package com.xxx.gulimall.product.service.impl;

import com.xxx.gulimall.product.dao.AttrDao;
import com.xxx.gulimall.product.entity.AttrEntity;
import com.xxx.gulimall.product.vo.BaseAttrVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.common.utils.PageUtils;
import com.xxx.common.utils.Query;

import com.xxx.gulimall.product.dao.ProductAttrValueDao;
import com.xxx.gulimall.product.entity.ProductAttrValueEntity;
import com.xxx.gulimall.product.service.ProductAttrValueService;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Autowired
    private AttrDao attrDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveAttrs(Long id, List<BaseAttrVO> attrEntities) {
        List<ProductAttrValueEntity> collect = attrEntities.stream().map(attrEntity -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setSpuId(id);
            productAttrValueEntity.setAttrId(attrEntity.getAttrId());
            productAttrValueEntity.setAttrName(attrDao.selectById(attrEntity.getAttrId()).getAttrName());
            productAttrValueEntity.setAttrValue(attrEntity.getAttrValues());
            productAttrValueEntity.setQuickShow(attrEntity.getShowDesc());
            return productAttrValueEntity;
        }).collect(Collectors.toList());

        this.saveBatch(collect);
    }

    @Override
    public List<ProductAttrValueEntity> getSpuList(Long spuId) {
        List<ProductAttrValueEntity> entities = this.list(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));

        return entities;
    }
}