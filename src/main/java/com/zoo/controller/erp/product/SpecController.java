package com.zoo.controller.erp.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.erp.product.SpecGroup;
import com.zoo.model.erp.product.SpecParam;
import com.zoo.service.erp.product.SpecGroupService;
import com.zoo.service.erp.product.SpecParamService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/product/spec")
public class SpecController {
	@Autowired
	SpecGroupService groupService;
	@Autowired
	SpecParamService paramService;
	@GetMapping("group")
	public List<SpecGroup> getGroupByType(String typeId){
		return groupService.getGroupByTypeId(typeId);
	}
	@GetMapping("getParamsByGroupId")
	public List<SpecParam> getParamsByGroupId(String groupId){
		return paramService.getParamsByGroupId(groupId);
	}
	@GetMapping("params")
	public List<SpecParam> querySpecParams(@RequestParam(value = "typeId", required = false) String typeId){
		return paramService.querySpecParams(typeId);
	}
	@PostMapping("addGroup")
	public RespBean addGroup(SpecGroup group) {
		try {
			groupService.addGroup(group);
			return new RespBean("200","添加成功");
		} catch (Exception e) {
			return new RespBean("500","添加失败");
		}
	}
	@PutMapping("updateGroup")
	public RespBean updateGroup(SpecGroup group) {
		try {
			groupService.updateGroup(group);
			return new RespBean("200","更新成功");
		} catch (Exception e) {
			return new RespBean("500","更新失败");
		}
	}
	@PostMapping("addParam")
	public RespBean addParam(SpecParam param) {
		try {
			paramService.addParam(param);
			return new RespBean("200","添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new RespBean("500",e.getMessage());
		}
	}
	@PutMapping("updateParam")
	public RespBean updateParam(SpecParam param) {
		try {
			paramService.updateParam(param);
			return new RespBean("200","更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new RespBean("500",e.getMessage());
		}
	}
}
