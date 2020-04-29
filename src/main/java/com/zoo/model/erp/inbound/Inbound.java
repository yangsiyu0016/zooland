package com.zoo.model.erp.inbound;

import java.util.Date;

import com.zoo.model.erp.cost.Cost;
import com.zoo.model.erp.warehouse.Warehouse;

import lombok.Data;
@Data
public class Inbound {
	private String id;
	private String code;
	private Date ctime;
	private String cuserId;
	private System cuser;
	private Warehouse warehouse;
	private String type;
	private String foreignKey;
	private Cost cost;
	
}
