package com.zoo.model.crm;

import java.util.List;

import com.zoo.model.system.area.Area;

import lombok.Data;

@Data
public class Receiving {
	private String id;
	private String countryId;//国家
	private String provinceId;//省份
	private String cityId;//城市
	private String countyId;//地区
	private String address;//收货地址
	private String consignee; //收货人
	private String cellphone;//联系电话
	private String customerId;
	private Area country;
	private Area province;
	private Area city;
	private Area county;
	private List<String> area;
	private String receivingContext;
	private String description;
}
