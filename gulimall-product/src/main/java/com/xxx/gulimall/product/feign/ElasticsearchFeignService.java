package com.xxx.gulimall.product.feign;

import com.xxx.common.to.SkuEsTO;
import com.xxx.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("gulimallelasticsearch")
public interface ElasticsearchFeignService {

    @PostMapping("elasticsearch/save")
    public R save(@RequestBody List<SkuEsTO> skuEsTOs);
}
