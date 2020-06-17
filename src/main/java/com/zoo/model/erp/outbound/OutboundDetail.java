package com.zoo.model.erp.outbound;

import java.math.BigDecimal;
import java.util.Date;

import com.zoo.model.erp.product.Product;
import com.zoo.model.erp.warehouse.GoodsAllocation;

import lombok.Data;
@Data
public class OutboundDetail {
	private String id;
	private String outboundId;
	private Product product;
	private GoodsAllocation goodsAllocation;
	private String orderDetailId;//表明来自订单详情的哪一条
	private BigDecimal number;
	private BigDecimal price;
	private BigDecimal totalMoney;
	private Date ctime;
}
