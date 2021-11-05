package com.xxx.gulimall.ware.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("gulimallproduct")
public interface ProductFeign {

    @GetMapping("product/skuinfo/name")
    public String getSkuName(@RequestBody Long skuId);
}
