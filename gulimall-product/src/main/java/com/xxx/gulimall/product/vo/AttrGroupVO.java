package com.xxx.gulimall.product.vo;

import com.xxx.gulimall.product.entity.AttrEntity;
import com.xxx.gulimall.product.entity.AttrGroupEntity;
import lombok.Data;

import java.util.List;

@Data
public class AttrGroupVO extends AttrGroupEntity {

    private List<AttrEntity> attrs;
}
