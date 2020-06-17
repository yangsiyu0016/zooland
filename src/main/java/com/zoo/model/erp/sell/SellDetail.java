package com.zoo.model.erp.sell;

import java.math.BigDecimal;
import java.util.Date;

import com.zoo.model.erp.product.Product;
import com.zoo.model.erp.warehouse.Warehouse;

import lombok.Data;

@Data
public class SellDetail {
	private String id;
	private Product product;
	private Warehouse warehouse;
	private BigDecimal number;
	private BigDecimal notOutNumber;
	private BigDecimal price;
	private BigDecimal totalMoney;
	
	private String sellId;
	private Date ctime;
}
