package com.zoo.mapper.erp.purchase;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.purchase.Contact;

@Component
public interface ContactMapper {
	
	//根据id获取供货地址联系人
	List<Contact> getContactById(@Param("id") String id);
	
	//新增供货联系人
	public void add(@Param("contact") Contact contact);

	//修改供货联系人
	public int update(@Param("contact") Contact contact);
	
}
