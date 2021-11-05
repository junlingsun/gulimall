package com.xxx.gulimall.product.feign;

import com.xxx.common.to.SkuReductionTO;
import com.xxx.common.to.SpuBoundsTO;
import com.xxx.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("gulimallcoupon")
public interface CouponFeignServices {

    @RequestMapping("coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundsTO spuBoundsTO);

    @RequestMapping("coupon/skufullreduction/save/reduction")
    R saveSkuReduction(@RequestBody SkuReductionTO skuReductionTO);
}
