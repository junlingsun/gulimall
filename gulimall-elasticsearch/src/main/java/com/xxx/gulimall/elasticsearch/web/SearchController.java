package com.xxx.gulimall.elasticsearch.web;

import com.xxx.gulimall.elasticsearch.service.ElasticsearchService;
import com.xxx.gulimall.elasticsearch.vo.SearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.xxx.gulimall.elasticsearch.vo.SearchParam;
import com.xxx.gulimall.elasticsearch.vo.SearchResp;

@Controller
public class SearchController {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @GetMapping("/")
    public String index(){

        return "search";
    }


    @GetMapping("/list.html")
    public String query(SearchParam searchParam, Model model) {
        SearchResp resp = elasticsearchService.search(searchParam);
        model.addAttribute("result", resp);
//        System.out.println("products" + resp.getProducts().get(0));
        return "search";
    }
}
