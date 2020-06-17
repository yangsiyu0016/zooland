package com.zoo.model.erp.purchase;

import java.math.BigDecimal;
import java.util.Date;

import com.zoo.model.erp.product.Product;
import com.zoo.model.erp.warehouse.Warehouse;

import lombok.Data;
@Data
public class PurchaseDetail {
	private String id;
	private Product product;
	private Warehouse warehouse;
	private BigDecimal number;
	private BigDecimal price;
	private BigDecimal totalMoney;
	
	private Date ctime;
	private String purchaseId;
	
	private BigDecimal notInNumber;
	private BigDecimal notOutNumber;
	
}
