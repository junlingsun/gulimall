package com.xxx.gulimall.thirdparty.controller;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestController
public class OssController {

    @Value("${alibaba.cloud.access-key}")
    private String accessId;
    @Value("${alibaba.cloud.oss.endpoint}")
    private String endpoint;
    @Value("${alibaba.cloud.oss.bucket-name}")
    private String bucket;

    @Autowired
    private OSS ossClient;

    @GetMapping("/oss/policy")
    public Map<String, String> policy(){

        String host = "https://" + bucket + "." + endpoint; // Specify the host in the bucketname.endpoint format.
        // Set the URL of the application server to which an upload callback request is sent. Replace the IP address and port number with your actual information.
//        String callbackUrl = "http://88.88.88.88:8888";
        String date = new SimpleDateFormat("MM-dd_yyyy").format(new Date());
        String dir = date + "/"; // Specify the prefix of the name of the object to be uploaded.
        Map<String, String> respMap = new LinkedHashMap<String, String>();


        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            // You can upload an object up to 5 GB in size by using PostObject. To upload an object that is 5 GB in size, set CONTENT_LENGTH_RANGE to 5*1024*1024*1024.
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);


            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            // respMap.put("expire", formatISO8601Date(expiration));



        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            System.out.println(e.getMessage());
        } finally {
            ossClient.shutdown();
        }
        return respMap;

    }
}
