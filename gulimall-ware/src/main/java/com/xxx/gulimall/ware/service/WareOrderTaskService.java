package com.xxx.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.common.utils.PageUtils;
import com.xxx.gulimall.ware.entity.WareOrderTaskEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author Junling Sun
 * @email junlingsun1983@gmail.com
 * @date 2021-09-24 00:08:14
 */
public interface WareOrderTaskService extends IService<WareOrderTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryTask(Map<String, Object> params);
}

