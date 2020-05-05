package com.zoo.controller.erp.sell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.erp.sell.SellDetail;
import com.zoo.service.erp.sell.SellDetailService;
import com.zoo.vo.RespBean;
@RestController
@RequestMapping("/erp/sell/detail")
public class SellDetailController {
	@Autowired
	SellDetailService detailService;
	
	@GetMapping("getNotOutDetailsBySellId")
	public List<SellDetail> getNotOutDetailsBySellId(String sellId){
		return detailService.getNotOutDetailBySellId(sellId);
	}
	@PostMapping("add")
	public Map<String,Object> add(@RequestBody SellDetail detail){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			detail = detailService.addDetail(detail);
			map.put("status", 200);
			map.put("detail", detail);
		} catch (Exception e) {
			map.put("status", 500);
			map.put("msag", e.getMessage());
		}
		return map;
	}
	@PutMapping("update")
	public RespBean update(@RequestBody SellDetail detail) {
		try {
			detailService.updateDetail(detail);
			return new RespBean("200","更新成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
	@RequestMapping(value = "{ids}",method=RequestMethod.DELETE)
	public RespBean deleteDetailById(@PathVariable String ids) {
		try {
			detailService.deleteDetailById(ids);
			return new RespBean("200","删除成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
}
