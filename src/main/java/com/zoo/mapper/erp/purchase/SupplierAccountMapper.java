package com.zoo.mapper.erp.purchase;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.purchase.SupplierAccount;

@Component
public interface SupplierAccountMapper {
	//根据供应商id获取其下所有开户信息
	public List<SupplierAccount> getSupplierAccountsById(@Param("id") String id);
	
	//插入开户信息
	public void addSupplierAccount(@Param("supplierAccount") SupplierAccount supplierAccount);
	
	//修改开户信息
	
	public void updateSupplierAccount(@Param("supplierAccount") SupplierAccount supplierAccount);
}
