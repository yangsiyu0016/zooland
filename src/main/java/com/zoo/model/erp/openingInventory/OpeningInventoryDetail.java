package com.zoo.model.erp.openingInventory;

import java.math.BigDecimal;
import java.util.Date;

import com.zoo.model.erp.product.ProductSku;
import com.zoo.model.erp.warehouse.GoodsAllocation;

import lombok.Data;

@Data
public class OpeningInventoryDetail {
	private String id;
	private ProductSku productSku;
	private GoodsAllocation goodsAllocation;
	private String openingInventoryId;
	private BigDecimal number;
	private BigDecimal costPrice;
	private BigDecimal totalMoney;
	private Date ctime;
}
