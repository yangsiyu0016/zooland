package com.zoo.model.erp.product;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MaterialStructureDetail {
	private String id;
	private String meterialStuctureId;
	private Product product;
	private BigDecimal number;
	private String description;
}
