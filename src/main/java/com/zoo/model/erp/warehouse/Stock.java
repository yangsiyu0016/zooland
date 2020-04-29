package com.zoo.model.erp.warehouse;

import java.math.BigDecimal;

import com.zoo.model.erp.product.ProductSku;

import lombok.Data;

@Data
public class Stock {
	private String id;
	private ProductSku productSku;
	private Warehouse warehouse;
	private BigDecimal usableNumber;
	private BigDecimal lockedNumber;
	private BigDecimal costPrice;
	private BigDecimal totalMoney;
	
}
