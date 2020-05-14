package com.zoo.controller.system.company;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.system.company.CompanyType;
import com.zoo.service.system.company.CompanyTypeService;
import com.zoo.vo.RespBean;


@RestController
@RequestMapping("/company/type")
public class CompanyTypeController {
	@Autowired
	CompanyTypeService companyTypeService;
	@GetMapping("/page")
	public Map<String,Object> queryCompanyTypeByPage(@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size){
		Map<String,Object> map = new HashMap<String,Object>();
		List<CompanyType> types = companyTypeService.getCompanyTypeByPage(page, size);
		Long count = companyTypeService.getCount();
		map.put("types", types);
		map.put("count", count);
		return map;
	}
	@GetMapping("/all")
	public List<CompanyType> getAll(){
		return companyTypeService.getAllCmpanyType();
	}
	@PostMapping("/add")
	public ResponseEntity<Void> add(CompanyType type){
		Boolean boo = companyTypeService.addCompanyType(type);
		if(boo==null||!boo) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	@PutMapping("update")
	public RespBean update(CompanyType type) {
		try {
			companyTypeService.updateCompanyType(type);
			return new RespBean("200","更新成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
	@PutMapping("/updateResource")
	public RespBean updateResource(String typeId,String[] menuIds) {
		try {
			companyTypeService.updateResource(typeId, menuIds);
			return new RespBean("success","操作成功");
		} catch (Exception e) {
			return new RespBean("error",e.getMessage());
		}
	}
	@GetMapping("/getResource")
	public List<String> getResource(String typeId) {
		//Map<String,Object> map = new HashMap<String,Object>();
		return companyTypeService.getResourceByTypeId(typeId);
	}
}
