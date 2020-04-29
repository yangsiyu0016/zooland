package com.zoo.model.erp.cost;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.zoo.model.erp.warehouse.Warehouse;
import com.zoo.model.system.base.Express;

import lombok.Data;
@Data
public class Cost {
	private String id;
	private String foreignKey;
	private BigDecimal money = new BigDecimal("0");
	private Express express;
	private String logisticsNumber;
	
	private Date ctime;
	private String fromType;
	
	private List<CostDetail> details;
	
	private Warehouse warehouse;
}
