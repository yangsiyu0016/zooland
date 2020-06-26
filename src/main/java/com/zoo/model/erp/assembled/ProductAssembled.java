package com.zoo.model.erp.assembled;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zoo.model.erp.product.Product;
import com.zoo.model.erp.warehouse.Warehouse;
import com.zoo.model.system.user.SystemUser;

import lombok.Data;

@Data
public class ProductAssembled {
	private String id;
	private String code;
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date assembledTime;
	private Product product;
	private Warehouse warehouse;
	private BigDecimal number;
	private String description;
	private String cuserId;
	private SystemUser cuser;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date ctime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date etime;
	private String companyId;
	private List<ProductAssembledMaterial> materials;
	private String codeGeneratorType;
	private String status;
	private String isClaimed;
	private String processInstanceId;
}
