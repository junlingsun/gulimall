package com.xxx.gulimall.ware.service.impl;

import com.xxx.common.constant.PurchaseStatus;
import com.xxx.gulimall.ware.vo.PurchaseCompleteVO;
import com.xxx.gulimall.ware.vo.PurchaseDetailVO;
import com.xxx.gulimall.ware.vo.PurchaseVO;
import com.xxx.gulimall.ware.entity.PurchaseDetailEntity;
import com.xxx.gulimall.ware.entity.WareSkuEntity;
import com.xxx.gulimall.ware.service.PurchaseDetailService;
import com.xxx.gulimall.ware.service.WareSkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.common.utils.PageUtils;
import com.xxx.common.utils.Query;

import com.xxx.gulimall.ware.dao.PurchaseDao;
import com.xxx.gulimall.ware.entity.PurchaseEntity;
import com.xxx.gulimall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Autowired
    private WareSkuService wareSkuService;



    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<PurchaseEntity> getUnreceivedList() {

        QueryWrapper<PurchaseEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("status", PurchaseStatus.CREATED.getStatus())
                .or().eq("status", PurchaseStatus.ASSIGNED.getStatus());

        List<PurchaseEntity> list = baseMapper.selectList(wrapper);
        return list;
    }

    @Transactional
    @Override
    public void mergeItems(PurchaseVO purchaseVO) {
        Long purchaseId = purchaseVO.getPurchaseId();
        List<Long> purchaseDetailIds = purchaseVO.getItems();

        if (purchaseId == null) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setStatus(PurchaseStatus.CREATED.getStatus());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> detailEntities = purchaseDetailService.listByIds(purchaseDetailIds);

        detailEntities.forEach(entity -> {
            entity.setStatus(PurchaseStatus.ASSIGNED.getStatus());
            entity.setPurchaseId(finalPurchaseId);
        });

        purchaseDetailService.updateBatchById(detailEntities);

    }

    @Transactional
    @Override
    public void purchaseComplete(PurchaseCompleteVO vo) {

        Long purchaseId = vo.getId();
        PurchaseEntity purchaseEntity = this.getById(purchaseId);
        Boolean flag = true;
        List<PurchaseDetailVO> items = vo.getItems();
        List<PurchaseDetailEntity> detailEntities = new ArrayList<>();
        List<Long> completeItemIds = new ArrayList<>();
        for (PurchaseDetailVO item: items) {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            if (item.getStatus() == PurchaseStatus.ABNORMAL.getStatus()) {
                flag = false;
            }else {
                completeItemIds.add(item.getItemId());
            }
            detailEntity.setStatus(item.getStatus());
            detailEntity.setId(item.getItemId());
            detailEntities.add(detailEntity);
        }
        purchaseDetailService.updateBatchById(detailEntities);
        if (flag) {
            purchaseEntity.setStatus(PurchaseStatus.COMPLETED.getStatus());
        }else {
            purchaseEntity.setStatus(PurchaseStatus.ABNORMAL.getStatus());
        }
        this.updateById(purchaseEntity);

        List<PurchaseDetailEntity> completeItems = purchaseDetailService.listByIds(completeItemIds);

        completeItems.forEach(item->{
//            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            Long skuId = item.getSkuId();
            QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("sku_id", skuId).eq("ware_id", item.getWareId());
            List<WareSkuEntity> wareSkuEntities = wareSkuService.list(wrapper);

            if (wareSkuEntities == null || wareSkuEntities.isEmpty()) {
                WareSkuEntity wareSkuEntity = new WareSkuEntity();
                wareSkuEntity.setStock(item.getSkuNum());
                wareSkuEntity.setSkuId(item.getSkuId());
                wareSkuEntity.setWareId(item.getWareId());
//                wareSkuEntity.setSkuName(item.get);
                wareSkuService.save(wareSkuEntity);

            }else {
                for (WareSkuEntity wareSkuEntity: wareSkuEntities) {
                    wareSkuEntity.setStock(wareSkuEntity.getStock()+item.getSkuNum());
                    wareSkuService.updateById(wareSkuEntity);
                }

            }
        });

    }

    @Override
    public void purchaseReceived(List<Long> purchaseIds) {
        List<PurchaseEntity> purchaseEntities = this.listByIds(purchaseIds);
        List<PurchaseEntity> purchaseEntityList = new ArrayList<>();
        purchaseEntities.forEach(entity->{
            if (entity.getStatus() == PurchaseStatus.ASSIGNED.getStatus() || entity.getStatus() == PurchaseStatus.CREATED.getStatus()) {
                entity.setStatus(PurchaseStatus.PICKED.getStatus());
                entity.setUpdateTime(new Date());
                purchaseEntityList.add(entity);
            }

        });

        this.updateBatchById(purchaseEntityList);

        purchaseIds.forEach(purchaseId ->{
            QueryWrapper<PurchaseDetailEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("purchase_id", purchaseId);

            List<PurchaseDetailEntity> detailEntities = purchaseDetailService.list(wrapper);
            detailEntities.forEach(entity -> {
                entity.setStatus(PurchaseStatus.PICKED.getStatus());
            });

            purchaseDetailService.updateBatchById(detailEntities);
        });




    }
}