package com.xxx.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.xxx.gulimall.product.entity.ProductAttrValueEntity;
import com.xxx.gulimall.product.service.AttrAttrgroupRelationService;
import com.xxx.gulimall.product.service.ProductAttrValueService;
import com.xxx.gulimall.product.vo.AttrRespVO;
import com.xxx.gulimall.product.vo.AttrVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xxx.gulimall.product.entity.AttrEntity;
import com.xxx.gulimall.product.service.AttrService;
import com.xxx.common.utils.PageUtils;
import com.xxx.common.utils.R;



/**
 * ??Ʒ?
 *
 * @author Junling Sun
 * @email junlingsun1983@gmail.com
 * @date 2021-09-23 22:02:52
 */
@Slf4j
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService valueService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }

    @RequestMapping("/{type}/list/{catId}")
    //@RequiresPermissions("product:attr:list")
    public R list(@PathVariable("type") Integer type, @PathVariable("catId") Long catId, @RequestParam Map<String, Object> params){


        PageUtils page = attrService.queryPage(params, type, catId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
		AttrRespVO attrVO = attrService.getAttrVOById(attrId);

        return R.ok().put("attr", attrVO);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVO attrVO){

		attrService.saveAttrVo(attrVO);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrRespVO attrRespVO){

		attrService.updateRespVoById(attrRespVO);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

    @GetMapping("/base/listforspu/{spuid}")
    public R getSpuList(@PathVariable("spuid") Long spuId) {
        List<ProductAttrValueEntity> valueEntities = valueService.getSpuList(spuId);

        return R.ok().put("data", valueEntities);

    }

}
