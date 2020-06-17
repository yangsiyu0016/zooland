package com.zoo.model.erp.statistics;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class SearchData {

	private String code;//查询单号
	private String status;//查询状态
	private	String supplierName;//供应商名称
	private String customerName;//客户名称
	private String productSkuId;//skuId
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date startDate;//开始时间
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date endDate;//结束时间
	private Integer page;//当前选中页数
	private Integer size;//煤业显示多少条
	
}
