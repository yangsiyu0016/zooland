package com.zoo.controller.crm.customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.zoo.model.crm.Receiving;
import com.zoo.service.crm.customer.ReceivingService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/customer/receiving")
public class ReceivingController {
	@Autowired
	ReceivingService receivingService;
	@GetMapping("page")
	public Map<String,Object> page(@RequestParam("page")Integer page,@RequestParam("size")Integer size,@Param("customerId")String customerId){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Receiving> receivings = receivingService.getReceivingByPage(page,size,customerId);
		long count = receivingService.getCount(customerId);
		map.put("receivings", receivings);
		map.put("count", count);
		return map;
	}
	@GetMapping("getReceivingsByCustomerId")
	public List<Receiving> getReceivingsByCustomerId(String customerId){
		return receivingService.getReceivingByCustomerId(customerId);
	}
	@PostMapping("addReceiving")
	public Map<String,Object> addReceiving(@RequestBody Receiving receiving) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			receiving = receivingService.addReceiving(receiving);
			map.put("status", 200);
			map.put("receiving", receiving);
		} catch (Exception e) {
			map.put("status", 500);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	@PutMapping("updateReceiving")
	public RespBean updateReceiving(@RequestBody  Receiving receiving) {
		try {
			receivingService.updateReceiving(receiving);
			return new RespBean("200","更新成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
}
