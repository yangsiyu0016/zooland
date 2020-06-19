package com.zoo.controller.erp.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.exception.ZooException;
import com.zoo.model.erp.product.MaterialStructure;
import com.zoo.service.erp.product.MaterialStructureService;
import com.zoo.vo.RespBean;
@RestController
@RequestMapping("/product/ms")
public class MaterialStructureController {
	@Autowired
	MaterialStructureService msService;
	@GetMapping("page")
	public Map<String,Object> getMaterialStructureByPage(@RequestParam("page")Integer page,@RequestParam("size")Integer size){
		Map<String,Object> map = new HashMap<String,Object>();
		List<MaterialStructure> mss = msService.getMaterialStructureByPage(page, size);
		Long count = msService.getCount();
		map.put("boms", mss);
		map.put("count", count);
		return map;
	}
	@PostMapping("add")
	public Map<String,Object> addMaterialStructure(@RequestBody MaterialStructure ms) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			ms = msService.addMaterialStructure(ms);
			resultMap.put("status", 200);
			resultMap.put("ms", ms);
		} catch (ZooException e) {
			resultMap.put("status", 500);
			resultMap.put("msg", e.getMsg());
		}
		return resultMap;
	}
	@PutMapping("update")
	public Map<String,Object> updateMaterialStructure(@RequestBody MaterialStructure ms) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			ms = msService.updateMaterialStructure(ms);
			resultMap.put("status", 200);
			resultMap.put("ms", ms);
		} catch (ZooException e) {
			resultMap.put("status", 500);
			resultMap.put("msg", e.getMsg());
		}
		return resultMap;
	}
	@GetMapping("getMS")
	public MaterialStructure getMaterialStructureByProductId(@RequestParam("productId")String productId) {
		return msService.getMaterialStructureByProductId(productId);
	}
	@GetMapping("getMSById")
	public MaterialStructure getMaterialStructureById(@RequestParam("id")String id) {
		return msService.getMaterialStructureById(id);
	}
	@RequestMapping(value = "{ids}",method=RequestMethod.DELETE)
	public RespBean deleteMaterialStructureById(@PathVariable String ids) {
		try {
			msService.deleteMaterialStructureById(ids);
			return new RespBean("200","删除成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
}
