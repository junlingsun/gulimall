package com.xxx.gulimall.elasticsearch.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxx.common.to.SkuEsTO;
import com.xxx.gulimall.elasticsearch.config.GuliElasticsearchConfig;
import com.xxx.gulimall.elasticsearch.constant.IndexConstant;
import com.xxx.gulimall.elasticsearch.service.ElasticsearchService;
import com.xxx.gulimall.elasticsearch.vo.SearchParam;
import com.xxx.gulimall.elasticsearch.vo.SearchResp;
import org.apache.lucene.search.TotalHits;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service("ElasticsearchService")
public class ElasticsearchServiceImpl implements ElasticsearchService {

    @Autowired
    private RestHighLevelClient client;


    @Override
    public void save(List<SkuEsTO> skuEsTOs) throws IOException {

        BulkRequest request = new BulkRequest();

        skuEsTOs.forEach(skuEsTO -> {
            IndexRequest indexRequest = new IndexRequest(IndexConstant.PRODUCT_INDEX.getName());
            indexRequest.id(skuEsTO.getSkuId().toString());
            String jsonString = JSON.toJSONString(skuEsTO);
            indexRequest.source(jsonString, XContentType.JSON);
            request.add(indexRequest);
        });

        client.bulk(request, GuliElasticsearchConfig.COMMON_OPTIONS);
    }

    @Override
    public SearchResp search(SearchParam searchParam) {
        SearchResp resp = null;

            try{
                SearchResponse search = client.search(searchRequest(searchParam), GuliElasticsearchConfig.COMMON_OPTIONS);
                resp = buildSearchResp(search, searchParam.getPageNum());
            }catch(Exception e){
                e.printStackTrace();
            }

            return resp;


    }

    private SearchResp buildSearchResp(SearchResponse response, int pageNum) {
        SearchResp searchResp = new SearchResp();

        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();

        List<SkuEsTO> skuEsTOS = new ArrayList<>();
        if (searchHits != null && searchHits.length > 0) {
            for (SearchHit hit: searchHits) {
                String source = hit.getSourceAsString();
                SkuEsTO skuEsTO = JSON.parseObject(source, SkuEsTO.class);
                if (hit.getHighlightFields().containsKey("skuName")) {
                    skuEsTO.setSkuName((hit.getHighlightFields().get("skuName").getFragments()[0]).toString());
                }

                skuEsTOS.add(skuEsTO);
            }
        }
        searchResp.setProducts(skuEsTOS);


        List<SearchResp.BrandVO> brandVOS = new ArrayList<>();
        ParsedLongTerms brandAggs = response.getAggregations().get("brand_aggs");
        List<? extends Terms.Bucket> brandBuckets = brandAggs.getBuckets();
        brandBuckets.forEach(bucket -> {
            SearchResp.BrandVO brandVO = new SearchResp.BrandVO();
            String brandId = bucket.getKeyAsString();
            brandVO.setBrandId(Long.parseLong(brandId));

            ParsedStringTerms brandNameAggs = bucket.getAggregations().get("brandName_aggs");
            String brandName = brandNameAggs.getBuckets().get(0).getKeyAsString();
            brandVO.setBrandName(brandName);

            ParsedStringTerms brandImgAggs = bucket.getAggregations().get("brandImg_aggs");
            String brandImg= brandImgAggs.getBuckets().get(0).getKeyAsString();
            brandVO.setBrandImg(brandImg);

            brandVOS.add(brandVO);
        });
        searchResp.setBrands(brandVOS);

        List<SearchResp.CatalogVO> catalogVOS = new ArrayList<>();
        ParsedLongTerms catalogAggs = response.getAggregations().get("catalog_aggs");
        List<? extends Terms.Bucket> CatalogBuckets = catalogAggs.getBuckets();
        CatalogBuckets.forEach(bucket -> {
            String key = bucket.getKeyAsString();
            SearchResp.CatalogVO catalogVO = new SearchResp.CatalogVO();
            catalogVO.setCatalogId(Long.parseLong(key));

            ParsedStringTerms catalogName = bucket.getAggregations().get("catalogName_aggs");
            String name = catalogName.getBuckets().get(0).getKeyAsString();
            catalogVO.setCatalogName(name);
            catalogVOS.add(catalogVO);
        });
        searchResp.setCatalogs(catalogVOS);

        List<SearchResp.AttrVO> attrVOS = new ArrayList<>();
        ParsedNested attrAggs = response.getAggregations().get("attr_aggs");
        ParsedLongTerms attrIdAggs = attrAggs.getAggregations().get("attrId_aggs");
        List<? extends Terms.Bucket> attrIdBuckets = attrIdAggs.getBuckets();
        attrIdBuckets.forEach(bucket -> {
            SearchResp.AttrVO attrVO = new SearchResp.AttrVO();
            String attrId = bucket.getKeyAsString();
            attrVO.setAttrId(Long.parseLong(attrId));

            ParsedStringTerms attrValueAggs = bucket.getAggregations().get("attrValue_aggs");
            List<? extends Terms.Bucket> attrValueBuckets = attrValueAggs.getBuckets();
            List<String> attrValues = new ArrayList<>();
            attrValueBuckets.forEach(valueBucket->{
                String value = valueBucket.getKeyAsString();
                attrValues.add(value);
            });
            attrVO.setAttrValues(attrValues);

            ParsedStringTerms attrNameAggs = bucket.getAggregations().get("attrName_aggs");
            String attrName = attrNameAggs.getBuckets().get(0).getKeyAsString();
            attrVO.setAttrName(attrName);

            attrVOS.add(attrVO);
        });
        searchResp.setAttrs(attrVOS);


        long totalRecords = hits.getTotalHits().value;
        searchResp.setTotalRecords(totalRecords);
        searchResp.setTotalPage(totalRecords%IndexConstant.PAGE_SIZE.getCode() == 0? (int)(totalRecords/IndexConstant.PAGE_SIZE.getCode()):
                (int)(totalRecords%IndexConstant.PAGE_SIZE.getCode()+1));
        searchResp.setPageNum(pageNum);


        return searchResp;
    }

    private SearchRequest searchRequest(SearchParam searchParam) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        if (StringUtils.hasLength(searchParam.getKeyword())) {
            boolQuery.must(QueryBuilders.matchQuery("skuName", searchParam.getKeyword()));
        }

        if (searchParam.getCatalogId() != null) {
            boolQuery.filter(QueryBuilders.termQuery("catalogId", searchParam.getCatalogId()));
        }

        if (searchParam.getBrandId() != null && (searchParam.getBrandId()).size() > 0) {
            boolQuery.filter(QueryBuilders.termsQuery("brandId", searchParam.getBrandId()));
        }

        System.out.println("param " + searchParam);

        if (searchParam.getAttrs() != null && searchParam.getAttrs().size() > 0) {
            for (String attr: searchParam.getAttrs()) {
                BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
                String[] s = attr.split("_");
                String attrId = s[0];
                String[] vals = s[1].split(":");
                boolQueryBuilder.filter(QueryBuilders.termQuery("attrs.attrId", s[0]));
                boolQueryBuilder.filter(QueryBuilders.termsQuery("attrs.attrValue", vals));
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", boolQueryBuilder, ScoreMode.Max);
                boolQuery.filter(nestedQuery);
            }
        }

        if (searchParam.getHasStock() != null && searchParam.getHasStock() > 0) {
            boolQuery.filter(QueryBuilders.termQuery("hasStock", searchParam.getHasStock()));
        }

        if (searchParam.getSkuPrice() != null) {
            String price = searchParam.getSkuPrice();
            if (price.indexOf("_") != 0) {
                String low = price.substring(0, price.indexOf("_"));
                String high = price.substring(price.indexOf("_")+1);
                boolQuery.filter(QueryBuilders.rangeQuery("price").gte(low).lte(high));
            }
        }

        searchSourceBuilder.query(boolQuery);

        if (StringUtils.hasLength(searchParam.getSort())) {
            String sort = searchParam.getSort();
            String[] s = sort.split("_");
            String sortItem = s[0];
            String sortOrder = s[1];
            searchSourceBuilder.sort(sortItem, sortOrder.equals("desc")? SortOrder.DESC:SortOrder.ASC);
        }

        searchSourceBuilder.from((searchParam.getPageNum()-1) * 2);
        searchSourceBuilder.size(2);

        if (StringUtils.hasLength(searchParam.getKeyword())) {
            System.out.println("highlight.........");
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuName");
            highlightBuilder.preTags("<b style='color: red'>");
            highlightBuilder.postTags("</b>");
            searchSourceBuilder.highlighter(highlightBuilder);
        }


        TermsAggregationBuilder brandAggs = AggregationBuilders.terms("brand_aggs");
        brandAggs.field("brandId").size(50);

        TermsAggregationBuilder brandNameAggs = AggregationBuilders.terms("brandName_aggs");
        brandNameAggs.field("brandName").size(50);
        brandAggs.subAggregation(brandNameAggs);

        TermsAggregationBuilder brandImgAggs = AggregationBuilders.terms("brandImg_aggs");
        brandImgAggs.field("brandImg").size(50);
        brandAggs.subAggregation(brandImgAggs);
        searchSourceBuilder.aggregation(brandAggs);

        TermsAggregationBuilder catalogAggs = AggregationBuilders.terms("catalog_aggs");
        catalogAggs.field("catalogId").size(50);
        TermsAggregationBuilder catalogNameAggs = AggregationBuilders.terms("catalogName_aggs");
        catalogNameAggs.field("catalogName").size(50);
        catalogAggs.subAggregation(catalogNameAggs);
        searchSourceBuilder.aggregation(catalogAggs);

        NestedAggregationBuilder nestedAggs = AggregationBuilders.nested("attr_aggs", "attrs");
        TermsAggregationBuilder attrIdAggs = new TermsAggregationBuilder("attrId_aggs");
        attrIdAggs.field("attrs.attrId").size(50);

        TermsAggregationBuilder attrValueAggs = new TermsAggregationBuilder("attrValue_aggs");
        attrValueAggs.field("attrs.attrValue").size(50);
        attrIdAggs.subAggregation(attrValueAggs);

        TermsAggregationBuilder attrNameAggs = new TermsAggregationBuilder("attrName_aggs");
        attrNameAggs.field("attrs.attrName").size(50);
        attrIdAggs.subAggregation(attrNameAggs);
        nestedAggs.subAggregation(attrIdAggs);
        searchSourceBuilder.aggregation(nestedAggs);

        System.out.println("searchsource " + searchSourceBuilder);

        SearchRequest searchRequest = new SearchRequest(new String[]{IndexConstant.PRODUCT_INDEX.getName()}, searchSourceBuilder);

        return searchRequest;

    }
}
