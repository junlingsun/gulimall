package com.xxx.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.common.utils.PageUtils;
import com.xxx.gulimall.ware.vo.PurchaseCompleteVO;
import com.xxx.gulimall.ware.vo.PurchaseVO;
import com.xxx.gulimall.ware.entity.PurchaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author Junling Sun
 * @email junlingsun1983@gmail.com
 * @date 2021-09-24 00:08:14
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<PurchaseEntity> getUnreceivedList();

    void mergeItems(PurchaseVO purchaseVO);

    void purchaseReceived(List<Long> purchaseIds);

    void purchaseComplete(PurchaseCompleteVO vo);
}

