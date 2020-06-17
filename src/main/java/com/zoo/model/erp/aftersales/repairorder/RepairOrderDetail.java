package com.zoo.model.erp.aftersales.repairorder;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zoo.model.erp.product.Product;

import lombok.Data;

@Data
public class RepairOrderDetail {

	private String id;
	private String repairOrderId;
	private String skuId;
	private Product product;//商品sku
	private String type;
	private BigDecimal number;//数量
	private BigDecimal materialCost;//材料费
	private BigDecimal totalMoney;//总额
	private BigDecimal manHourFee;//工时费
	private String repairResult;//维修结果
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date ctime;
	private String problemDescription;//问题描述
}
