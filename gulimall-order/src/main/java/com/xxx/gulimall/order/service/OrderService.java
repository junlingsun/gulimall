package com.xxx.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.common.utils.PageUtils;
import com.xxx.gulimall.order.entity.OrderEntity;

import java.util.Map;

/**
 * ????
 *
 * @author Junling Sun
 * @email junlingsun1983@gmail.com
 * @date 2021-09-24 00:03:43
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

