package com.zoo.model.erp.product;

import java.util.List;

import lombok.Data;

@Data
public class SpecGroup {
	private String id;
	private String typeId;
	private String name;
	private List<SpecParam> params;
}
