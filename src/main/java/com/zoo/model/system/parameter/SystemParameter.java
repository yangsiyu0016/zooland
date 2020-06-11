package com.zoo.model.system.parameter;

import lombok.Data;

@Data
public class SystemParameter {
	private String id;
	private String name;
	private String parameterValue;
	private String code;
	private String description;
	private String companyId;
	private SystemParameterDirectory parameterDirectory;
}
