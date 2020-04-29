package com.zoo.model.crm;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zoo.model.system.area.Area;
import com.zoo.model.system.user.SystemUser;

import lombok.Data;

@Data
public class Customer {
	private String id;
	private String name;
	private SystemUser owner;//客户拥有人
	private String countryId;
	private String provinceId;
	private String cityId;
	private String countyId;
	private String companyId;
	private List<Account> accounts;//开户行
	private List<Linkman> linkmans;//联系人
	private List<Receiving> receivings;//收货地址
	private List<String> area;
	private String areaPath;
	private String address;
	private String description;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date created;
	private String type;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date gtime;
	private String systemUserId;
	private SystemUser cuser;
	private Area country;
	private Area province;
	private Area city;
	private Area county;
}
