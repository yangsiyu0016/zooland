package com.zoo.model.erp.productsplit;

import java.math.BigDecimal;
import java.util.Date;

import com.zoo.model.erp.product.Product;
import com.zoo.model.erp.warehouse.GoodsAllocation;

import lombok.Data;

@Data
public class ProductSplitDetail {

	private String id;
	private Product product;//产品
	private BigDecimal number;//拆分单数
	private BigDecimal totalNumber;//拆分总数
	private String description;//备注
	private String productSplitId;//拆分单id
	private Date ctime;//创建时间
	private GoodsAllocation goodsAllocation;//入库货位
	private BigDecimal notInNumber;//未入库数量
}
