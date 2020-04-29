package com.zoo.model.erp.sell;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zoo.model.crm.Customer;
import com.zoo.model.crm.Receiving;
import com.zoo.model.system.user.SystemUser;

import lombok.Data;

@Data
public class Sell {
	private String id;
	private String code;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date initDate;
	private Customer customer;//客户
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date ctime;
	private Date etime;
	private String cuserId;
	private List<SellDetail> details;
	private String codeGeneratorType;
	private String status;
	private Receiving receiving;
	private String processInstanceId;
	private String companyId;
	private SystemUser cuser;
	
}
