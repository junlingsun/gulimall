package com.xxx.gulimall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.xxx.gulimall.ware.vo.PurchaseCompleteVO;
import com.xxx.gulimall.ware.vo.PurchaseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xxx.gulimall.ware.entity.PurchaseEntity;
import com.xxx.gulimall.ware.service.PurchaseService;
import com.xxx.common.utils.PageUtils;
import com.xxx.common.utils.R;



/**
 * 采购信息
 *
 * @author Junling Sun
 * @email junlingsun1983@gmail.com
 * @date 2021-09-24 00:08:14
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;


    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:purchase:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }

    @GetMapping("/unreceive/list")
    public R getUnreceiveList() {
        List<PurchaseEntity> list = purchaseService.getUnreceivedList();

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:purchase:info")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase){
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:purchase:delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @PostMapping("/merge")
    public R mergeItems(@RequestBody PurchaseVO purchaseVO){

        purchaseService.mergeItems(purchaseVO);
        return R.ok();

    }

    @PostMapping("/done")
    public R purchaseComplete(@RequestBody PurchaseCompleteVO vo)  {
        purchaseService.purchaseComplete(vo);

        return R.ok();
    }

}
