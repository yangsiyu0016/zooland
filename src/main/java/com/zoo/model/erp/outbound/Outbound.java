package com.zoo.model.erp.outbound;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date ctime;
	private String type;
	private Cost cost;
	private List<OutboundDetail> details;
}
