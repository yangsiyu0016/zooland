package com.zoo.model.erp.warehouse;

import java.util.Date;
import java.util.List;

import com.zoo.model.system.user.SystemUser;

import lombok.Data;

@Data
public class Warehouse {
	private String id;
	private String name;
	private String address;
	private String companyId;
	private Boolean status;
	private Date ctime;
	private List<SystemUser> managers;
	private List<GoodsAllocation> goodsAllocations;
}
