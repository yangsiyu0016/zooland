package com.zoo.model.erp;

import lombok.Data;

@Data
public class GeneratorCode {
	private String id;
	private String type;
	private String pid;//获取产品分类 需提供大类ID
	private String initCode;//产品的 大类和小类编码组合
	private int number;
	private String companyId;
	
}
