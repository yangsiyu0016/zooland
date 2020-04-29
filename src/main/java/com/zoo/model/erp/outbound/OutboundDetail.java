package com.zoo.model.erp.outbound;

import java.math.BigDecimal;
import java.util.Date;

import com.zoo.model.erp.product.ProductSku;
import com.zoo.model.erp.warehouse.GoodsAllocation;

import lombok.Data;
@Data
public class OutboundDetail {
	private String id;
	private String outboundId;
	private ProductSku productSku;
	private GoodsAllocation goodsAllocation;
	private String orderDetailId;//表明来自订单详情的哪一条
	private BigDecimal number;
	private BigDecimal price;
	private BigDecimal totalMoney;
	private Date ctime;
}
