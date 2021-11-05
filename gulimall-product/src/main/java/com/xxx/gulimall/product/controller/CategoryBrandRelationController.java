package com.xxx.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.xxx.gulimall.product.entity.BrandEntity;
import com.xxx.gulimall.product.entity.CategoryEntity;
import com.xxx.gulimall.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xxx.gulimall.product.entity.CategoryBrandRelationEntity;
import com.xxx.gulimall.product.service.CategoryBrandRelationService;
import com.xxx.common.utils.PageUtils;
import com.xxx.common.utils.R;



/**
 * Ʒ?Ʒ???????
 *
 * @author Junling Sun
 * @email junlingsun1983@gmail.com
 * @date 2021-09-23 22:02:51
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;



    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:categorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }

    @RequestMapping("/catelog/list")
    public R relationList(@RequestParam("brandId") long brandId){

        List<CategoryBrandRelationEntity> relationEntities = categoryBrandRelationService.listByBrandId(brandId) ;

        return R.ok().put("data", relationEntities);
    }

    @RequestMapping("/brands/list")
    //@RequiresPermissions("product:categorybrandrelation:list")
    public R brandList(@RequestParam("catId") Long catId){
        List<BrandEntity> brandEntities = categoryBrandRelationService.getBrandList(catId);


        return R.ok().put("data", brandEntities);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:categorybrandrelation:save")
    public R save(@RequestBody CategoryBrandRelationEntity relationEntity){
        long brandId = relationEntity.getBrandId();

        long catelogId = relationEntity.getCatelogId();

		categoryBrandRelationService.save(brandId, catelogId);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
