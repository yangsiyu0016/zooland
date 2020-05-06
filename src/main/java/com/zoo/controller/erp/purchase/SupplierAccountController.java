package com.zoo.controller.erp.purchase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.erp.purchase.SupplierAccount;
import com.zoo.service.erp.purchase.SupplierAccountService;
import com.zoo.vo.RespBean;

@RequestMapping("erp/supplierAccount")
@RestController
public class SupplierAccountController {

	@Autowired
	private SupplierAccountService supplierAccountService;
	
	@GetMapping("getSupplierAccountsById")
	public Map<String, Object> getSupplierAccountsById(@RequestParam("id") String id){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			List<SupplierAccount> supplierAccounts = supplierAccountService.getSupplierAccountById(id);
			map.put("supplierAccounts", supplierAccounts);
			map.put("status", "200");
		} catch (Exception e) {
			// TODO: handle exception
			map.put("status", 500);
			map.put("msg", e.getMessage());
		}
		
		return map;
	}
	
	@PostMapping("addSupplierAccount")
	public Map<String, Object> addSupplierAccount(@RequestBody SupplierAccount supplierAccount) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			 supplierAccount = supplierAccountService.addSupplierAccount(supplierAccount);
			 
			 map.put("supplierAccount", supplierAccount);
			 map.put("status", "200");
			return map;
		} catch (Exception e) {
			// TODO: handle exception
			map.put("status", "500");
			map.put("msg", e.getMessage());
			return map;
		}
		
	}
	
	@PutMapping("updateSupplierAccount")
	public RespBean updateSupplierAccount(@RequestBody SupplierAccount supplierAccount) { 
		try {
			
			supplierAccountService.updateSupplierAccount(supplierAccount);
			
			return new RespBean("200", "更新成功");
			
		} catch (Exception e) {
			// TODO: handle exception
			
			return new RespBean("500", "更新失败");
			
		}
	}
	
}
