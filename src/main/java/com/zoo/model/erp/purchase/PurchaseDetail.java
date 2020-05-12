package com.zoo.model.erp.purchase;

import java.math.BigDecimal;
import java.util.Date;

import com.zoo.model.erp.product.ProductSku;
import com.zoo.model.erp.warehouse.Warehouse;

import lombok.Data;
@Data
public class PurchaseDetail {
	private String id;
	private ProductSku productSku;
	private Warehouse warehouse;
	private BigDecimal number;
	private BigDecimal price;
	private BigDecimal totalMoney;
	
	private Date ctime;
	private String purchaseId;
	
	private BigDecimal notInNumber;
	private BigDecimal notOutNumber;
	
}
