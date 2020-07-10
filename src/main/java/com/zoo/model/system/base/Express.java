package com.zoo.model.system.base;

import java.util.Date;

import lombok.Data;
@Data
public class Express {
	private String id;
	private String name;
	private String description;
	private String phone;
	private String startAddress;
	private String type;
	private Date ctime;
	private String companyId;
}
