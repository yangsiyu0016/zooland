package com.zoo.model.erp;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zoo.model.erp.warehouse.Stock;

import lombok.Data;

@Data
public class JournalAccount {
	private String id;
	private String type;
	private String orderDetailId;
	private String orderCode;
	private Stock stock;
	private BigDecimal rkNumber;
	private BigDecimal rkPrice;
	private BigDecimal rkTotalMoney;
	private BigDecimal ckNumber;
	private BigDecimal ckPrice;
	private BigDecimal ckTotalMoney;
	private BigDecimal totalNumber;
	private BigDecimal costPrice;
	private BigDecimal totalMoney;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date ctime;
	private String customerId;
	private String companyId;
}
