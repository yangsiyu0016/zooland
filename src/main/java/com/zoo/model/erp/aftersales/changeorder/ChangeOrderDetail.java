package com.zoo.model.erp.aftersales.changeorder;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zoo.model.erp.product.Product;

import lombok.Data;

@Data
public class ChangeOrderDetail {

	private String id;
	private String changeOrderId;
	private String skuId;
	private Product product;//商品sku
	private String type;
	private BigDecimal number;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date ctime;
	
}
