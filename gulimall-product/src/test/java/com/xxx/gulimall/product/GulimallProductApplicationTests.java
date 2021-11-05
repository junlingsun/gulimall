package com.xxx.gulimall.product;

import com.xxx.gulimall.product.entity.BrandEntity;
import com.xxx.gulimall.product.service.BrandService;
import com.xxx.gulimall.product.service.SpuInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GulimallProductApplicationTests {

	@Autowired
	BrandService brandService;

	@Autowired
	SpuInfoService spuInfoService;

	@Test
	void contextLoads() {

		BrandEntity brandEntity = new BrandEntity();
		brandEntity.setName("Huawei");
		brandService.save(brandEntity);
	}

	@Test
	void updateStatus(){
		spuInfoService.updateProductStatus(33l);
	}

}
