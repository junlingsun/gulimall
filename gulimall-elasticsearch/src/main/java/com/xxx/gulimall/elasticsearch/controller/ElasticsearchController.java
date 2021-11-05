package com.xxx.gulimall.elasticsearch.controller;


import com.xxx.common.to.SkuEsTO;
import com.xxx.common.utils.R;
import com.xxx.gulimall.elasticsearch.service.ElasticsearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
public class ElasticsearchController {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @ResponseBody
    @PostMapping("elasticsearch/save")
    public R save(@RequestBody List<SkuEsTO> skuEsTOs) throws IOException {
        elasticsearchService.save(skuEsTOs);

        return R.ok();
    }
}
