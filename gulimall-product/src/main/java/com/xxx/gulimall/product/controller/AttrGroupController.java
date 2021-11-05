package com.xxx.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.xxx.common.valid.AddGroup;
import com.xxx.common.valid.UpdateGroup;
import com.xxx.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.xxx.gulimall.product.entity.AttrEntity;
import com.xxx.gulimall.product.service.AttrAttrgroupRelationService;
import com.xxx.gulimall.product.service.AttrService;
import com.xxx.gulimall.product.vo.AttrGroupVO;
import com.xxx.gulimall.product.vo.AttrVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xxx.gulimall.product.entity.AttrGroupEntity;
import com.xxx.gulimall.product.service.AttrGroupService;
import com.xxx.common.utils.PageUtils;
import com.xxx.common.utils.R;



/**
 * ???Է??
 *
 * @author Junling Sun
 * @email junlingsun1983@gmail.com
 * @date 2021-09-23 22:02:51
 */

@Slf4j
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService relationService;

    /**
     * 列表
     */
    @RequestMapping("/list/{catId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@PathVariable("catId") long catId, @RequestParam Map<String, Object> params){
        PageUtils page = attrGroupService.queryPage(params, catId);



        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
//		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        AttrGroupEntity attrGroup = attrGroupService.getAttrGroupById(attrGroupId);
        return R.ok().put("attrGroup", attrGroup);
    }


    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@Validated(value = {AddGroup.class}) @RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@Validated(value = {UpdateGroup.class}) @RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

    @RequestMapping("/attr/relation/delete")

    public R delete(@RequestBody List<AttrAttrgroupRelationEntity> list){
        attrGroupService.removeRelationByIds(list);

        return R.ok();
    }

    @RequestMapping("/{attrGroupId}/attr/relation")
    public R getRelation(@PathVariable("attrGroupId") Long attrGroupId) {

        List<AttrEntity> list = attrGroupService.getAttr(attrGroupId);
        return R.ok().put("data", list);
    }

    @RequestMapping("/{attrGroupId}/noattr/relation")
    public R getNonAttr(@RequestParam Map<String, Object> param, @PathVariable("attrGroupId") Long attrGroupId) {
        PageUtils pageUtils = attrService.getNonAttr(param, attrGroupId);
        return R.ok().put("page", pageUtils);
    }

    @RequestMapping("/attr/relation")
    public R getNonAttr(@RequestBody List<AttrAttrgroupRelationEntity> relationEntities) {
        relationService.addRelation(relationEntities);
        return R.ok();
    }

    @RequestMapping("/{catelogId}/withattr")
    public R getAttr(@PathVariable("catelogId") Long catelogId) {

        List<AttrGroupVO> attrGroupVOs = attrGroupService.getAttrGroupVOs(catelogId);
        return R.ok().put("data", attrGroupVOs);
    }



}
