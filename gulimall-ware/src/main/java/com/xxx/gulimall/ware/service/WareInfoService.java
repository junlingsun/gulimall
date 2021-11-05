package com.xxx.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.common.utils.PageUtils;
import com.xxx.gulimall.ware.entity.WareInfoEntity;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author Junling Sun
 * @email junlingsun1983@gmail.com
 * @date 2021-09-24 00:08:14
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryWareInfo(Map<String, Object> params);
}

