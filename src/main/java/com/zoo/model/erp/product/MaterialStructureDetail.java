package com.zoo.model.erp.product;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class MaterialStructureDetail {
	private String id;
	private String materialStructureId;
	private Product product;
	private BigDecimal number;
	private String description;
	
	private List<MaterialStructureDetail> details;
}
