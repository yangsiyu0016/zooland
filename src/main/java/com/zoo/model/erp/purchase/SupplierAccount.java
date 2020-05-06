package com.zoo.model.erp.purchase;

import lombok.Data;

@Data
public class SupplierAccount {
	
	private String id;//主键id
	private String supplierId;//主表id
	private String type;//类型
	private String bankNumber;//银行卡号
	private String bankName;//开户行名称
	private String accountName;//开户名称
	private String accountContext;
}
