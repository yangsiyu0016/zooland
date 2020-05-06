package com.zoo.service.erp.purchase;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.erp.purchase.SupplierAccountMapper;
import com.zoo.model.erp.purchase.SupplierAccount;

@Service
public class SupplierAccountService {
	
	@Autowired
	private SupplierAccountMapper supplierAccountMapper;
	
	//根据供应商id获取开户信息
	public List<SupplierAccount> getSupplierAccountById( String id) {
		List<SupplierAccount> accounts = supplierAccountMapper.getSupplierAccountsById(id);
		for(SupplierAccount account:accounts) {
			account.setAccountContext(account.getBankNumber()+" | "+account.getBankName()+" | "+account.getAccountName());
		}
		return accounts;
		
	}
	
	//添加开户信息
	public SupplierAccount addSupplierAccount(SupplierAccount supplierAccount) {
		
		supplierAccount.setId(UUID.randomUUID().toString());
		
		supplierAccountMapper.addSupplierAccount(supplierAccount);
		
		return supplierAccount;
		
	}
	
	//修改开户信息
	public void updateSupplierAccount(SupplierAccount supplierAccount) {
		
		supplierAccountMapper.updateSupplierAccount(supplierAccount);
		
	}
	
}
