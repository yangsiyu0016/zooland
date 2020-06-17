package com.zoo.model.erp.cost;

import java.math.BigDecimal;
import java.util.List;

import com.zoo.model.erp.product.Product;
import com.zoo.model.erp.warehouse.Warehouse;

import lombok.Data;
@Data
public class CostDetail {
	private String id;
	private String detailId;//对应的产品详情表
	private String costId;
	private Product product;
	private BigDecimal number = new BigDecimal("0");
	private BigDecimal price = new BigDecimal("0");
	private BigDecimal totalMoney = new BigDecimal("0");
	private Warehouse warehouse;
	private List<CostDetailGoodsAllocation> cdgas;
	
}
