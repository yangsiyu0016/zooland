package com.zoo.controller.erp.purchase;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.erp.purchase.Contact;
import com.zoo.model.erp.purchase.SupplyAddress;
import com.zoo.service.erp.purchase.SupplyAddressService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/erp/supplyAddress")
public class SupplyAddressController {

	@Autowired
	private SupplyAddressService supplyAddressService;
	
	@PostMapping("addSupplyAddress")
	public Map<String,Object> addSupplyAddress(@RequestBody SupplyAddress supplyAddress) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			supplyAddress = supplyAddressService.addSupplyAddress(supplyAddress);
			map.put("status", 200);
			map.put("supplyAddress", supplyAddress);
		} catch (Exception e) {
			map.put("status", 500);
			map.put("msg", e.getMessage());
			
		}
		return map;
	}
	@PutMapping("updateSupplyAddress")
	public RespBean updateSupplyAddress(@RequestBody SupplyAddress supplyAddress) {
		try {
			int updateLinkman = supplyAddressService.updateSupplyAddress(supplyAddress);
			return new RespBean("200","更新成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
	
}
