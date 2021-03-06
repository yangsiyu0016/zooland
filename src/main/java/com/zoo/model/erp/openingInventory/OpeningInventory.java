package com.zoo.model.erp.openingInventory;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zoo.model.annex.Annex;
import com.zoo.model.erp.warehouse.Warehouse;
import com.zoo.model.system.user.SystemUser;

import lombok.Data;

@Data
public class OpeningInventory {
	private String id;
	private String code;
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date initDate;
	
	private Warehouse warehouse;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date ctime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date etime;
	private String cuserId;
	private String warehouseId;
	private SystemUser cuser;
	private String companyId;
	private String processInstanceId;
	private String status;
	private String codeGeneratorType;
	private List<OpeningInventoryDetail> details;
	private String isClaimed;
	
	private List<Annex> annexs;
	
}
