package com.zoo.controller.erp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.erp.JournalAccount;
import com.zoo.service.erp.JournalAccountService;

@RestController
@RequestMapping("/erp/ja")
public class JournalAccountController {
	@Autowired
	JournalAccountService jaService;
	@GetMapping("page")
	public Map<String,Object> page(
			@RequestParam("page")Integer page,
			@RequestParam("size")Integer size,
			@RequestParam("keywords")String keywords,
			@RequestParam("code")String code,
			@RequestParam("productCode")String productCode,
			@RequestParam("productName")String productName,
			@RequestParam("warehouseId")String warehouseId,
			@RequestParam("sort")String sort,
			@RequestParam("order")String order){
		Map<String,Object> map = new HashMap<String,Object>();
		List<JournalAccount> journalAccounts = jaService.getJournalAccountByPage(page, size,keywords,code,productCode,productName,warehouseId,sort,order);
		
		long count = jaService.getCount(keywords,code,productCode,productName,warehouseId);
		map.put("journalAccounts", journalAccounts);
		map.put("count", count);
		return map;
	}
}
