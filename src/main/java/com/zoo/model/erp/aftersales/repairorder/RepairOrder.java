package com.zoo.model.erp.aftersales.repairorder;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zoo.model.crm.Customer;
import com.zoo.model.erp.purchase.Supplier;
import com.zoo.model.system.base.Express;
import com.zoo.model.system.user.SystemUser;

import lombok.Data;

/**
 * 维修单实体
 * @author aa
 *
 */
@Data
public class RepairOrder {

	private String id;
	private String code;
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date initDate;
	private String customerId;
	private String supplierId;
	private String status;
	private String cuserId;
	private String processInstanceId;
	private String companyId;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date ctime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date etime;
	private String isClaimed;
	private SystemUser cuser;
	private Customer customer;
	private Supplier supplier;
	private List<RepairOrderDetail> details;
	private String codeGeneratorType;
	private String reason;
	private Express express;//物流信息
}
