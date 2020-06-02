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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date ctime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
	private Date etime;
	private String cuserId;
	private List<SellDetail> details;
	private String codeGeneratorType;
	private String status;
	private Receiving receiving;
	private String processInstanceId;
	private String companyId;
	private SystemUser cuser;
	private String receivingContext;
	private String isClaimed;
	
	private String receivableType;//付款方式
	private String freightType;//运费类型 YES:包邮 NO：不包邮
	
	private String description;//备注
}
