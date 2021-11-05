package com.xxx.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.xxx.common.valid.AddGroup;
import com.xxx.common.valid.ListValues;
import com.xxx.common.valid.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

/**
 * Ʒ?
 * 
 * @author Junling Sun
 * @email junlingsun1983@gmail.com
 * @date 2021-09-22 23:22:23
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Ʒ??id
	 */

	@Null (groups = {AddGroup.class})
	@NotNull(groups = {UpdateGroup.class})
	@TableId
	private Long brandId;
	/**
	 * Ʒ???
	 */
	@NotNull (groups = {AddGroup.class, UpdateGroup.class})
	private String name;
	/**
	 * Ʒ??logo??ַ
	 */
	@NotNull(groups = {AddGroup.class})
	@URL(groups = {AddGroup.class, UpdateGroup.class})
	private String logo;
	/**
	 * ???
	 */
	@NotNull(groups = {AddGroup.class, UpdateGroup.class})
	private String descript;
	/**
	 * ??ʾ״̬[0-????ʾ??1-??ʾ]
	 */

	@ListValues(values = {0, 1})
	private Integer showStatus;
	/**
	 * ????????ĸ
	 */
	@NotNull(groups = {AddGroup.class})
	@Pattern(regexp = "^[a-zA-Z]$", groups = {AddGroup.class, UpdateGroup.class})
	private String firstLetter;
	/**
	 * ???
	 */
	@NotNull(groups = {AddGroup.class})
	@Min(value = 0, groups = {AddGroup.class, UpdateGroup.class})
	private Integer sort;

}
