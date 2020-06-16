package com.zoo.model.erp.warehouse;

import java.math.BigDecimal;
import java.util.List;

import com.zoo.model.erp.product.Product;
import lombok.Data;

@Data
public class Stock {
	private String id;
	private Product product;
	private Warehouse warehouse;
	private BigDecimal usableNumber;
	private BigDecimal lockedNumber;
	private BigDecimal costPrice;
	private BigDecimal totalMoney;
	private List<StockDetail> details;
}
