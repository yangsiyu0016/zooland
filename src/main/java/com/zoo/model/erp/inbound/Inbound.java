package com.zoo.model.erp.inbound;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zoo.model.erp.cost.Cost;
import com.zoo.model.erp.productsplit.ProductSplitDetail;
import com.zoo.model.erp.warehouse.Warehouse;
import com.zoo.model.system.user.SystemUser;

import lombok.Data;
@Data
public class Inbound {
	private String id;
	private String code;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date ctime;
	private String cuserId;
	private SystemUser cuser;
	private Warehouse warehouse;
	private String type;
	private String foreignKey;
	private Cost cost;
	private ProductSplitDetail splitDetail;
	private List<InboundDetail> details;
}
