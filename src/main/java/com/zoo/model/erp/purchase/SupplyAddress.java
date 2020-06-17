package com.zoo.model.erp.purchase;

import java.util.List;

import com.zoo.model.system.area.Area;

import lombok.Data;

//供货地址
@Data
public class SupplyAddress {
	
	private String id; //主键
	private String countryId;//地区id
	private String provinceId;//省份id
	private String cityId;//城市id
	private String countyId;//国家id
	private List<String> area;
	private String address;//供货地址
	private String supply;//供货人
	private String phone;//联系电话
	private String supplierId;//供货商id
	private String beat;//选择的区域
	private Area country;//地区
	private Area province;//省份
	private Area city;//城市
	private Area county;//国家
	private String remarks;//备注
	
}
