package com.zoo.model.erp.inventorycheck;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zoo.model.erp.warehouse.Warehouse;
import com.zoo.model.system.user.SystemUser;

import lombok.Data;

@Data
public class InventoryCheck {

	private String id;
	private String code;//单号
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date initDate;//订单日期
	private String warehouseId;//仓库id
	private String cuserId;//创建人id
	private SystemUser cuser;//创建人对象
	private String companyId;//公司id
	private String status;//订单状态
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date ctime;//创建时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date etime;//完成时间
	private String processInstanceId;//流程id
	private Warehouse warehouse;
	private String codeGeneratorType;//单号生成方式
	private List<InventoryCheckDetail> details;
	
}
