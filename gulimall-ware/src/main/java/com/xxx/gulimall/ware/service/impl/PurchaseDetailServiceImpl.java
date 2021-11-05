package com.xxx.gulimall.ware.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.common.utils.PageUtils;
import com.xxx.common.utils.Query;

import com.xxx.gulimall.ware.dao.PurchaseDetailDao;
import com.xxx.gulimall.ware.entity.PurchaseDetailEntity;
import com.xxx.gulimall.ware.service.PurchaseDetailService;
import org.springframework.util.StringUtils;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                new QueryWrapper<PurchaseDetailEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPurchaseDetail(Map<String, Object> params) {
        String key = (String)params.get("key");
        String status = (String)params.get("status");
        String wareId = (String)params.get("wareId");

        QueryWrapper<PurchaseDetailEntity> wrapper = new QueryWrapper<>();

        if (StringUtils.hasLength(wareId)) {
            wrapper.eq("ware_id", wareId);
        }
        if (StringUtils.hasLength(status)) {
            wrapper.eq("status", status);
        }

        if (StringUtils.hasLength(key)) {
            wrapper.and(obj->{
                obj.eq("id", key)
                        .or().eq("purchase_id", key)
                        .or().eq("sku_id", key)
                        .or().eq("sku_num", key);
            });
        }

        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                wrapper
        );


        return new PageUtils(page);
    }
}