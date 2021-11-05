package com.xxx.gulimall.ware.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.common.utils.PageUtils;
import com.xxx.common.utils.Query;

import com.xxx.gulimall.ware.dao.WareOrderTaskDao;
import com.xxx.gulimall.ware.entity.WareOrderTaskEntity;
import com.xxx.gulimall.ware.service.WareOrderTaskService;
import org.springframework.util.StringUtils;


@Service("wareOrderTaskService")
public class WareOrderTaskServiceImpl extends ServiceImpl<WareOrderTaskDao, WareOrderTaskEntity> implements WareOrderTaskService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<WareOrderTaskEntity> page = this.page(
                new Query<WareOrderTaskEntity>().getPage(params),
                new QueryWrapper<WareOrderTaskEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryTask(Map<String, Object> params) {

        String key = (String)params.get("key");
        QueryWrapper<WareOrderTaskEntity> wrapper = new QueryWrapper<>();
        if (StringUtils.hasLength(key)) {
            wrapper.eq("id", key)
                    .or().eq("order_id", key)
                     .or().like("consignee", key)
                    .or().like("delivery_address", key);
        }

        IPage page = this.page(
                new Query<WareOrderTaskEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }
}