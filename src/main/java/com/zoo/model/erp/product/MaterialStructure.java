package com.zoo.model.erp.product;

import java.util.List;

import lombok.Data;

@Data
public class MaterialStructure {
	private String id;
	private Product product;
	private String description;
	private String companyId;
	private List<MaterialStructureDetail> details;
}
