package com.zoo.controller.erp.aftersales.returnorder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.erp.aftersales.returnorder.ReturnOrderDetail;
import com.zoo.service.erp.aftersales.returnorder.ReturnOrderDetailService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/erp/returnOrder/detail")
public class ReturnOrderDetailController {

	@Autowired
	ReturnOrderDetailService detailService;
	
	@RequestMapping("getDtailByReturnOrderId")
	public List<ReturnOrderDetail> getDtailByReturnOrderId(@RequestParam("returnOrderId") String returnOrderId) {
		return detailService.getDetailByReturnOrderId(returnOrderId);
	}
	
	@PostMapping("add")
	public Map<String, Object> addDetail(@RequestBody ReturnOrderDetail detail) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			detail = detailService.addDetail(detail);
			map.put("status", 200);
			map.put("detail", detail);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("status", 500);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	
	@PutMapping("update")
	public RespBean updateDetail(@RequestBody ReturnOrderDetail detail) {
		try {
			detailService.updateDetail(detail);
			return new RespBean("200", "更新成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@RequestMapping(value = "{ids}", method = RequestMethod.DELETE)
	public RespBean deleteDetailById(@PathVariable String ids) {
		try {
			detailService.deleteDetail(ids);
			return new RespBean("200", "删除成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
}
