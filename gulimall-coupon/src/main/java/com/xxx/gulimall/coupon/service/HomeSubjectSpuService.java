package com.xxx.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.common.utils.PageUtils;
import com.xxx.gulimall.coupon.entity.HomeSubjectSpuEntity;

import java.util.Map;

/**
 * ר????Ʒ
 *
 * @author Junling Sun
 * @email junlingsun1983@gmail.com
 * @date 2021-09-23 23:32:47
 */
public interface HomeSubjectSpuService extends IService<HomeSubjectSpuEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

