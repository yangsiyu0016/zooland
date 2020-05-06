package com.zoo.model.erp.purchase;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zoo.model.system.area.Area;
import com.zoo.model.system.user.SystemUser;

import lombok.Data;
//供货商实体类
@Data
public class Supplier {
	
	private String id; //主键
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date createTime;//创建时间
	private String supplierAddress;//供货商地址
	private String supplierTelphone;//供货商电话
	private String supplierName;//供货商名称
	private String supplierEmail;//供货商邮箱
	private String cellphone;//联系电话
	private String type;//类型
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date gtime;//获取时间
	private String countryId;//国家id
	private String cityId;//城市id
	private String provinceId;//省份id
	private String countyId;//地区id
	private SystemUser systemUser;//创建人
	private String systemUserId;//创建人id
	private SystemUser owner;//拥有人
	private String ownerId;//拥有人
	private String companyId;//公司id
	private List<String> area;//选择区域集合
	private Area country;//国家
	private Area city;//城市
	private Area province;//省份id
	private Area county;//地区id
	private String beat;//选择的区域
	private List<Contact> contacts;//联系人集合
	private List<SupplierAccount> supplierAccounts;//供货地址集合
	private String remarks;//备注
}
