package com.xxx.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.xxx.common.valid.AddGroup;
import com.xxx.common.valid.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * ???Է??
 * 
 * @author Junling Sun
 * @email junlingsun1983@gmail.com
 * @date 2021-09-22 23:22:23
 */
@Data
@TableName("pms_attr_group")
public class AttrGroupEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ????id
	 */
	@NotNull (groups = {UpdateGroup.class})
	@Null(groups = {AddGroup.class})
	@TableId
	private Long attrGroupId;
	/**
	 * ???
	 */
	@NotNull (groups = {AddGroup.class, UpdateGroup.class})
	private String attrGroupName;
	/**
	 * ???
	 */
	@NotNull (groups = {AddGroup.class, UpdateGroup.class})
	@Min(value = 0, groups = {AddGroup.class, UpdateGroup.class})
	private Integer sort;
	/**
	 * ???
	 */

	@NotNull (groups = {AddGroup.class, UpdateGroup.class})
	private String descript;
	/**
	 * ??ͼ?
	 */
	@NotNull (groups = {AddGroup.class, UpdateGroup.class})
//	@URL(groups = {AddGroup.class, UpdateGroup.class})
	private String icon;
	/**
	 * ????????id
	 */
	@NotNull (groups = {AddGroup.class, UpdateGroup.class})
	private Long catelogId;

	@TableField(exist = false)
	private List<Long> catelogPath;

}
