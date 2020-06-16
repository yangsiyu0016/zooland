package com.zoo.model.erp.aftersales.replenishmentnote;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zoo.model.erp.product.ProductSku;
import com.zoo.model.system.base.Express;

import lombok.Data;

@Data
public class ReplenishmentNoteDetail {

	private String id;
	private String replenishmentNoteId;
	private String skuId;
	private ProductSku sku;//商品sku
	private String type;
	private BigDecimal number;
	private BigDecimal costPrice;
	private BigDecimal totalMoney;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date ctime;
	private String reason;
	private String problemDescription;
	private Express express;//物流信息
}
