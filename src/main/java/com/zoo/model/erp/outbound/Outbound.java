package com.zoo.model.erp.outbound;

import java.util.Date;
import java.util.List;

import com.zoo.model.erp.cost.Cost;
import com.zoo.model.erp.warehouse.Warehouse;

import lombok.Data;

@Data
public class Outbound {
	private String id;
	private String code;
	private String foreignKey;
	private String cuserId;
	private Warehouse warehouse;
	private Date ctime;
	private String type;
	private Cost cost;
	private List<OutboundDetail> details;
}
