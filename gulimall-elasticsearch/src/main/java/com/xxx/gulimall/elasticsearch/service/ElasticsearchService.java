package com.xxx.gulimall.elasticsearch.service;

import com.xxx.common.to.SkuEsTO;
import com.xxx.gulimall.elasticsearch.vo.SearchParam;
import com.xxx.gulimall.elasticsearch.vo.SearchResp;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


public interface ElasticsearchService {
    void save(List<SkuEsTO> skuEsTOs) throws IOException;

    SearchResp search(SearchParam searchParam);
}
