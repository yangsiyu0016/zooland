package com.zoo.controller.erp.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

import com.zoo.exception.ZooException;
import com.zoo.model.erp.product.MaterialStructureDetail;
import com.zoo.service.erp.product.MaterialStructureDetailService;
import com.zoo.vo.RespBean;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product/ms/detail")
public class MaterialStructureDetailController {
	@Autowired
	MaterialStructureDetailService detailService;
	
	@PutMapping("add")
	public Map<String,Object> addDetail(@RequestBody MaterialStructureDetail detail){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			detail = detailService.addMaterialStructureDetail(detail);
			resultMap.put("status", 200);
			resultMap.put("detail", detail);
		} catch (ZooException e) {
			resultMap.put("status", 500);
			resultMap.put("msg", e.getMsg());
		}
		return resultMap;
	}
	@PutMapping("update")
	public Map<String,Object> updateDetail(@RequestBody MaterialStructureDetail detail){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			detail = detailService.updateMaterialStructureDetail(detail);
			resultMap.put("status", 200);
			resultMap.put("detail", detail);
		} catch (ZooException e) {
			resultMap.put("status", 500);
			resultMap.put("msg", e.getMsg());
		}
		return resultMap;
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
