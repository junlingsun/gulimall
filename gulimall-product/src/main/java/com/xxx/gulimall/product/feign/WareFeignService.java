package com.xxx.gulimall.product.feign;

import com.xxx.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@FeignClient(value = "gulimallware")
public interface WareFeignService {

    @PostMapping("ware/waresku/stock")
    Map<Long, Boolean> stockInfo(@RequestBody List<Long> skuIds);
}
