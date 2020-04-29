package com.zoo.model.erp.warehouse;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class StockDetail {
	private String id;
	private String stockId;
	private GoodsAllocation goodsAllocation;
	private BigDecimal usableNumber;
	private BigDecimal lockedNumber;
}
