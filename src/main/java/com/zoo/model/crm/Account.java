package com.zoo.model.crm;

import lombok.Data;

@Data
public class Account {
	private String id;
	private String type;
	private String bankNumber; //银行账号
	private String bankName;//开户银行
	private String accountName;//开户人
	private String customerId;

	public String getAccountContext() {
		return this.bankNumber+","+this.accountName+","+this.bankName;
	}
}
