package com.xxx.gulimall.coupon.service.impl;

import com.xxx.common.to.MemberPrice;
import com.xxx.common.to.SkuReductionTO;
import com.xxx.gulimall.coupon.dao.SkuLadderDao;
import com.xxx.gulimall.coupon.entity.MemberPriceEntity;
import com.xxx.gulimall.coupon.entity.SkuLadderEntity;
import com.xxx.gulimall.coupon.service.MemberPriceService;
import com.xxx.gulimall.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
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

import com.xxx.gulimall.coupon.dao.SkuFullReductionDao;
import com.xxx.gulimall.coupon.entity.SkuFullReductionEntity;
import com.xxx.gulimall.coupon.service.SkuFullReductionService;
import org.springframework.transaction.annotation.Transactional;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    private SkuLadderService skuLadderService;

    @Autowired
    private MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSkuReduction(SkuReductionTO skuReductionTO) {
        //save ladder info
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        BeanUtils.copyProperties(skuReductionTO, skuLadderEntity);
        skuLadderService.save(skuLadderEntity);

        //save full reduction
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTO, skuFullReductionEntity);
        baseMapper.insert(skuFullReductionEntity);

        //save member price
        List<MemberPrice> memberPriceList = skuReductionTO.getMemberPrice();
        if (memberPriceList == null || memberPriceList.isEmpty()) return;
        List<MemberPriceEntity> memberPriceEntities = memberPriceList.stream().map(memberPrice -> {
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setMemberPrice(memberPrice.getPrice());
            memberPriceEntity.setMemberLevelName(memberPrice.getName());
            memberPriceEntity.setSkuId(skuReductionTO.getSkuId());
            memberPriceEntity.setMemberLevelId(memberPrice.getId());
            return memberPriceEntity;
        }).collect(Collectors.toList());

        memberPriceService.saveBatch(memberPriceEntities);
    }
}