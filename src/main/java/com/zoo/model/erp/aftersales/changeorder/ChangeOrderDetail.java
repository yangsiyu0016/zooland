package com.zoo.model.erp.aftersales.changeorder;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zoo.model.erp.product.ProductSku;
import com.zoo.model.system.base.Express;

import lombok.Data;

@Data
public class ChangeOrderDetail {

	private String id;
	private String changeOrderId;
	private String skuId;
	private ProductSku productSku;//商品sku
	private String type;
	private BigDecimal number;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date ctime;
	
}
