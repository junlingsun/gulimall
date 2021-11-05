package com.xxx.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.xxx.gulimall.product.entity.CategoryEntity;
import com.xxx.gulimall.product.service.CategoryService;
import com.xxx.common.utils.PageUtils;
import com.xxx.common.utils.R;



/**
 * 商品三级分类
 *
 * @author Junling Sun
 * @email junlingsun1983@gmail.com
 * @date 2021-09-23 22:02:51
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 列表
     */
    @RequestMapping("/treeList")
    //@RequiresPermissions("product:category:list")
    public R list(){
//        PageUtils page = categoryService.queryPage(params);
        List<CategoryEntity> tree = categoryService.buildTree();

        return R.ok().put("data", tree);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    //@RequiresPermissions("product:category:info")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */

    @RequestMapping("/update")
    //@RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.update(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/updateBatch")
    //@RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity[] categoryList){
        categoryService.updateBatchById(Arrays.asList(categoryList));

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:category:delete")
    public R delete(@RequestBody Long[] catIds){
//		categoryService.removeByIds(Arrays.asList(catIds));
        categoryService.deleteByIds(catIds);

        return R.ok();
    }





}
