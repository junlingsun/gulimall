package com.xxx.gulimall.elasticsearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class GulimallElasticsearchApplicationTests {
	@Autowired
	private RestHighLevelClient client;

	@Test
	void contextLoads() {

		System.out.println(client);
	}

	@Test
	void indexTest() throws IOException {
		IndexRequest request = new IndexRequest("test");
		request.id("1");

		User user = new User();
		user.setUserName("aaa");
		user.setAge(15);
		String jsonString = new ObjectMapper().writeValueAsString(user);
		request.source(jsonString, XContentType.JSON);
		IndexResponse response = client.index(request, RequestOptions.DEFAULT);
		System.out.println("response" + response);
	}

	@Data
	private class User{
		private  String userName;
		private Integer age;
	}

}
