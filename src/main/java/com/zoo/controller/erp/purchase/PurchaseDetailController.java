package com.zoo.controller.erp.purchase;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.erp.purchase.PurchaseDetail;
import com.zoo.service.erp.purchase.PurchaseDetailService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/erp/purchase/detail")
public class PurchaseDetailController {
	@Autowired
	PurchaseDetailService detailService;
	
	@PostMapping("add")
	public Map<String,Object> addDetail(@RequestBody PurchaseDetail detail) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			detail = detailService.addDetail(detail);
			map.put("status", 200);
			map.put("detail", detail);
		} catch (Exception e) {
			map.put("status", 500);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	@PutMapping("update")
	public RespBean updateDetail(@RequestBody PurchaseDetail detail) {
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
