package com.xxx.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxx.common.utils.PageUtils;
import com.xxx.gulimall.member.entity.MemberReceiveAddressEntity;

import java.util.Map;

/**
 * ??Ա?ջ???ַ
 *
 * @author Junling Sun
 * @email junlingsun1983@gmail.com
 * @date 2021-09-23 23:58:34
 */
public interface MemberReceiveAddressService extends IService<MemberReceiveAddressEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

