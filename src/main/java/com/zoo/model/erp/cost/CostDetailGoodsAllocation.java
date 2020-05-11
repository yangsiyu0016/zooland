package com.zoo.model.erp.cost;

import java.math.BigDecimal;

import com.zoo.model.erp.warehouse.GoodsAllocation;

import lombok.Data;

@Data
public class CostDetailGoodsAllocation {
	private String id;
	private String costDetailId;
	private GoodsAllocation goodsAllocation;
	private BigDecimal number;
}
