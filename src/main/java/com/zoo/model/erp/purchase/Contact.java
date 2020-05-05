package com.zoo.model.erp.purchase;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

//供货联系人对象
@Data
public class Contact {
	
	private String id; //主键
	private String name;//姓名
	private String supplyCall;//称呼
	private String sex;//性别
	private String position;//职位
	private String telephone;//座机
	private String callphone;//手机
	private String email;//邮箱
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date birthday;//生日
	private String supplierId;//供货商id
	private String remarks;//备注

}
