package com.zoo.model.erp.inbound;

import java.math.BigDecimal;
import java.util.Date;

import com.zoo.model.erp.product.ProductSku;
import com.zoo.model.erp.warehouse.GoodsAllocation;

import lombok.Data;
@Data
public class InboundDetail {
	private String id;
	private String inboundId;
	private ProductSku productSku;
	
	private GoodsAllocation goodsAllocation;
	private String orderDetailId;
	private Date ctime;
	private BigDecimal number;
	private BigDecimal price;
	private BigDecimal totalMoney;
}
