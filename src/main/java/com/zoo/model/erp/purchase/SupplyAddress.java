package com.zoo.model.erp.purchase;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zoo.model.system.area.Area;

import lombok.Data;

//供货地址
@Data
public class SupplyAddress {
	
	private String id; //主键
	private String countyId;//地区id
	private String cityId;//城市id
	private String provinceId;//省份id
	private String countryId;//国家id
	private List<String> area;
	private String address;//供货地址
	private String supply;//供货人
	private String phone;//联系电话
	private String supplierId;//供货商id
	private String beat;//选择的区域
	private Area county;//地区
	private Area city;//城市
	private Area province;//省份
	private Area country;//国家
	private String remarks;//备注
	
}
