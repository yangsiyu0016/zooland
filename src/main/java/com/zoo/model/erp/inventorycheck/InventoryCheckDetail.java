package com.zoo.model.erp.inventorycheck;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zoo.model.erp.product.ProductSku;
import com.zoo.model.erp.warehouse.GoodsAllocation;

import lombok.Data;

@Data
public class InventoryCheckDetail {

	private String id;
	private String panDianId;//盘点单ID
	private ProductSku productSku;//商品sku
	private String type;//类型：OVERFLOW:盘盈 LOSSES:盘损
	private BigDecimal number;//数量
	private GoodsAllocation goodsAllocation;//货位对象
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date ctime;//创建时间
	private BigDecimal costPrice;//成本价
	private BigDecimal totalMoney;//总额
	private String description;//备注
	private int occupyStock;//是否占用库存
	private BigDecimal occupyStockNumber;//占用库存数量
	
	private BigDecimal currentCostPrice;//当前库存成本价
	private BigDecimal currentTotalMoney;//当前总成本
	
}
