package com.xxx.gulimall.product.web;

import com.xxx.gulimall.product.entity.CategoryEntity;
import com.xxx.gulimall.product.service.CategoryService;
import com.xxx.gulimall.product.vo.Catalog2WebVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/")
    public String getIndexPage(Model model){

        List<CategoryEntity> categoryEntityList = categoryService.getCategory1List();


        model.addAttribute("catagories", categoryEntityList);
        return "index";
    }

    @ResponseBody
    @RequestMapping("index/catalog")
    public Map<Long, List<Catalog2WebVO>> getCatalog2List(Model model) {

        Map<Long, List<Catalog2WebVO>> map = categoryService.getCategory2List();
        return map;

    }
}
