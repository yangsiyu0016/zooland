package com.zoo.model.erp.statistics;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PurchaseStatistics {

	private String id;
	private String productId;//产品id
	private BigDecimal number;//数量
	private BigDecimal price;//单价
	private BigDecimal totalMoney;//总额
	private BigDecimal notInNumber;//未入库数量
	private String code;//单号
	private String supplierId;//供应商id
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date ctime;//创建时间
	private String status;//流程标识
	private String companyId;//公司id
	private String supplierName;//供应商
	private String productName;//产品名称
	private String productType;//产品类型
	private String spec;//规格
	private String cuserId;//创建人id
	private String realName;//创建人名称
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date initDate;//下单日期
	
}
