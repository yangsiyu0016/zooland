package com.zoo.model.erp.statistics;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PurchaseStatistics {

	private String id;
	private BigDecimal number;//数量
	private BigDecimal price;//单价
	private BigDecimal totalMoney;//总额
	private BigDecimal notInNumber;//未入库数量
	private String code;//单号
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date ctime;//创建时间
	private String status;//流程标识
	private String supplierName;//供应商
	private String genericSpec;//通用参数
	private String specialSpec;//特殊参数
	private String productName;//产品名称
	private String name;//产品类型
	private String ownSpec;
	private String companyId;
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date initDate;//下单日期
	
}
