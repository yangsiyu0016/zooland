package com.zoo.model.erp.purchase;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zoo.model.crm.Account;
import com.zoo.model.crm.Customer;
import com.zoo.model.system.user.SystemUser;

import lombok.Data;
@Data
public class Purchase {
	private String id;
	private String code;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date initDate;
	private Customer customer;
	private String cuserId;
	private String processInstanceId;
	private String status;
	private Account account;
	
	private Date ctime;
	private Date etime;
	
	private String codeGeneratorType;
	private String companyId;
	
	private List<PurchaseDetail> details;
	private SystemUser cuser;
}
