package com.zoo.model.system.parameter;

import java.util.List;

import lombok.Data;

@Data
public class SystemParameterDirectory {
	private String id;
	private String parentId;
	private String name;
	private int orderNumber;
	private String companyId;
	private List<SystemParameterDirectory> children;
	private SystemParameterDirectory parent;
}
