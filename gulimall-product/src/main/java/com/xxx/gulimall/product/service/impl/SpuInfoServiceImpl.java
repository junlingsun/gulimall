package com.xxx.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xxx.common.constant.PublishStatus;
import com.xxx.common.to.AttrTO;
import com.xxx.common.to.SkuEsTO;
import com.xxx.common.to.SkuReductionTO;
import com.xxx.common.to.SpuBoundsTO;
import com.xxx.gulimall.product.entity.*;
import com.xxx.gulimall.product.feign.CouponFeignServices;
import com.xxx.gulimall.product.feign.ElasticsearchFeignService;
import com.xxx.gulimall.product.feign.WareFeignService;
import com.xxx.gulimall.product.service.*;
import com.xxx.gulimall.product.vo.*;
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

import com.xxx.gulimall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    private SpuImagesService spuImagesService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private CouponFeignServices couponFeignServices;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private WareFeignService wareFeignService;

    @Autowired
    private ElasticsearchFeignService elasticsearchFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryDetail(Map<String, Object> params) {
        Long brandId = params.get("brandId") == null? null:Long.parseLong((String)params.get("brandId"));


        Long catelogId = params.get("catelogId") == null? null: Long.parseLong((String)params.get("catelogId"));
        String key = params.get("key") == null? null: (String)params.get("key");

        Integer status = null;
        if (params.get("status") != null && StringUtils.hasLength((String)params.get("status"))) {
            status = Integer.parseInt((String)params.get("status"));
        }


        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        if (brandId != null && brandId != 0) {
            wrapper.eq("brand_id", brandId);
        }
        if (catelogId != null && catelogId != 0) {
            wrapper.eq("catalog_id", catelogId);
        }

        if (status != null) {
            wrapper.eq("publish_status", status);
        }

        if (StringUtils.hasLength(key)) {
            wrapper.and(obj ->{
                obj.eq("id", key).or().eq("brand_id", key).or().eq("catalog_id", key).or().like("spu_name", key)
                        .or().like("spu_description", key);
            });
        }

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );


        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void updateProductStatus(Long spuId) {

        SpuInfoEntity spuInfoEntity = this.getById(spuId);
        CategoryEntity categoryEntity = categoryService.getById(spuInfoEntity.getCatalogId());
        BrandEntity brandEntity = brandService.getById(spuInfoEntity.getBrandId());
        List<ProductAttrValueEntity> productAttrValueEntities = productAttrValueService.list(
                new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        List<Long> attrIds = productAttrValueEntities.stream().map(entity -> {
            return entity.getAttrId();
        }).collect(Collectors.toList());

        List<AttrEntity> attrEntities = attrService.listByIds(attrIds);
        Set<Long> searchableAttrIds = new HashSet<>();
        attrEntities.forEach(entity->{
            if (entity.getSearchType() == 0) searchableAttrIds.add(entity.getAttrId()); //TODO: search type should be 1 for searchable.
        });



        List<AttrTO> attrTOs = new ArrayList<>();
        productAttrValueEntities.forEach(entity->{
                if (searchableAttrIds.contains(entity.getAttrId())) {
                    AttrTO attrTO = new AttrTO();
                    attrTO.setAttrId(entity.getAttrId());
                    attrTO.setAttrName(entity.getAttrName());
                    attrTO.setAttrValue(entity.getAttrValue());
                    attrTOs.add(attrTO);

                }
        });


        List<SkuInfoEntity> skuInfoEntities = skuInfoService.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        List<SkuEsTO> skuEsTOS = new ArrayList<>();
        List<Long> skuIds = skuInfoEntities.stream().map(entity -> {
            return entity.getSkuId();
        }).collect(Collectors.toList());

        Map<Long, Boolean> stockMap = wareFeignService.stockInfo(skuIds);

        skuInfoEntities.forEach(entity->{
            SkuEsTO skuEsTO = new SkuEsTO();

            skuEsTO.setBrandId(spuInfoEntity.getBrandId());
            skuEsTO.setCatalogId(spuInfoEntity.getCatalogId());
            skuEsTO.setSpuId(spuInfoEntity.getId());
            skuEsTO.setCatalogName(categoryEntity.getName());
            skuEsTO.setBrandName(brandEntity.getName());
            skuEsTO.setBrandImg(brandEntity.getLogo());

            //TODO: set hotScore. default value is 0

            BeanUtils.copyProperties(entity, skuEsTO);
            skuEsTO.setAttrs(attrTOs);

            //set stock information
            boolean hasStock = stockMap.get(entity.getSkuId());
            skuEsTO.setHasStock(hasStock);

            skuEsTOS.add(skuEsTO);


        });


        elasticsearchFeignService.save(skuEsTOS);

        spuInfoEntity.setPublishStatus(PublishStatus.PRODUCT_UP.getType());
        this.update(spuInfoEntity, new UpdateWrapper<SpuInfoEntity>().eq("id", spuId));
    }

    @Transactional
    @Override
    public void saveSpuInfo(SpuInfoVO spuInfoVO) {

        //save spuinfo
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuInfoVO, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.save(spuInfoEntity);


        //save spu descript info
        spuInfoDescService.saveSpuDescipt(spuInfoEntity.getId(), spuInfoVO.getDecript());


        //save spu images
        List<String> images = spuInfoVO.getImages();
        spuImagesService.saveImages(spuInfoEntity.getId(), images);


        //save base attrs
        List<BaseAttrVO> attrEntities = spuInfoVO.getBaseAttrs();
        productAttrValueService.saveAttrs(spuInfoEntity.getId(), attrEntities);


        //save Bounds
        Bounds bounds = spuInfoVO.getBounds();
        SpuBoundsTO spuBoundsTO = new SpuBoundsTO();
        BeanUtils.copyProperties(bounds, spuBoundsTO);
        spuBoundsTO.setSpuId(spuInfoEntity.getId());
        couponFeignServices.saveSpuBounds(spuBoundsTO);


        //save sku info
        List<SkuVO> skuVOS = spuInfoVO.getSkus();
        for (SkuVO skuVO: skuVOS) {
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            BeanUtils.copyProperties(skuVO, skuInfoEntity);
            skuInfoEntity.setSpuId(spuInfoEntity.getId());
            skuInfoEntity.setBrandId(spuInfoVO.getBrandId());
            skuInfoEntity.setCatalogId(spuInfoVO.getCatalogId());

            List<SkuImage> skuImages = skuVO.getImages();
            SkuImage defaultImage = new SkuImage();
            for (SkuImage skuImage: skuImages) {
                if (skuImage.getDefaultImg() == 1) {
                    defaultImage = skuImage;
                    break;
                }
            }

            //save images
            skuInfoEntity.setSkuDefaultImg(defaultImage.getImgUrl());
            skuInfoService.save(skuInfoEntity);
            List<SkuImagesEntity> skuImagesEntities = skuImages.stream().map(skuImage -> {
                SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                BeanUtils.copyProperties(skuImage, skuImagesEntity);
                skuImagesEntity.setSkuId(skuInfoEntity.getSkuId());
                return skuImagesEntity;
            }).collect(Collectors.toList());

            skuImagesService.saveBatch(skuImagesEntities);

            //save sale attrs
            List<SaleAttrVO> saleAttrVOS = skuVO.getAttr();
            List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = saleAttrVOS.stream().map(saleAttrVO -> {
                SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                skuSaleAttrValueEntity.setAttrValue(saleAttrVO.getAttrValues());
                skuSaleAttrValueEntity.setAttrId(saleAttrVO.getAttrId());
                skuSaleAttrValueEntity.setAttrName(saleAttrVO.getAttrName());
                skuSaleAttrValueEntity.setSkuId(skuInfoEntity.getSkuId());
                return skuSaleAttrValueEntity;
            }).collect(Collectors.toList());

            skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);
            log.info("save sku attr");


            //save sku reduction
            SkuReductionTO skuReductionTO = new SkuReductionTO();
            BeanUtils.copyProperties(skuVO, skuReductionTO);
            skuReductionTO.setSkuId(skuInfoEntity.getSkuId());
            couponFeignServices.saveSkuReduction(skuReductionTO);
        }






    }
}