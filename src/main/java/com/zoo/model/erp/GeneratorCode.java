package com.zoo.model.erp;

import lombok.Data;

@Data
public class GeneratorCode {
	private String id;
	private String prefix;
	private int length;
	private int number;
	private String dateStr;//日期参数值
	private String dateValue;//日期值
	private String companyId;
	
}
