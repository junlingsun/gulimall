package com.xxx.gulimall.thirdparty;

import com.aliyun.oss.OSS;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
class GulimallThirdpartyApplicationTests {

	@Autowired
	private OSS ossClient;

	@Test
	void contextLoads() throws FileNotFoundException {
		ossClient.putObject("gulimall-js", "pic1", new FileInputStream("/Users/junlingsun/Desktop/nane.gif"));
		ossClient.shutdown();
	}



}
