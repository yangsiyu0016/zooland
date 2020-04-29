package com.zoo.model.crm;

import java.util.Date;

import lombok.Data;

@Data
public class Linkman {
	private String id;
	private String realName;
	private String nikeName;
	private Integer sex;
	private String email;
	private String telphone;
	private String cellphone;
	private String job;
	private Date birthday;
	private String description;
	private String customerId;
}
