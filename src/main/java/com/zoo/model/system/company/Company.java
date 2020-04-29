package com.zoo.model.system.company;




import com.zoo.model.system.user.SystemUser;

import lombok.Data;

@Data
public class Company {
	private String id;
	private String name;
	private String managerId;
	private String description;
	private CompanyType companyType;
	private String companyTypeId;
	private SystemUser systemUser;
	private String userName;
}
