package com.xxx.gulimall.member.feign;

import com.xxx.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("gulimallcoupon")
public interface CouponFeign {

    @RequestMapping("coupon/coupon/member/list")
    public R couponList();
}
