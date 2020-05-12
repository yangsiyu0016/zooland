package com.zoo.model.erp.purchase;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zoo.model.annex.Annex;
import com.zoo.model.system.user.SystemUser;

import lombok.Data;
@Data
public class Purchase {
	private String id;
	private String code;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date initDate;
	private Supplier Supplier;
	private String cuserId;
	private String processInstanceId;
	private String status;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date ctime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date etime;
	
	private String codeGeneratorType;
	private String companyId;
	
	private List<PurchaseDetail> details;
	private SystemUser cuser;
	private SupplierAccount supplierAccount;
	private String accountContext;
	private List<Annex> annexs;
}
