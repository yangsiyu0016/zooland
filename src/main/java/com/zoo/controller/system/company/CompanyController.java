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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.system.company.Company;
import com.zoo.service.system.company.CompanyService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/company")
public class CompanyController {
	@Autowired
	CompanyService companyService;
	@GetMapping("page")
	public Map<String,Object> queryCompanyByPage(
			@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Company> companys = companyService.getCompanyByPage(page, size);
		Long count = companyService.getCount();
		map.put("companys",companys);
		map.put("count",count);
		return map;
	}
	@GetMapping("/all")
	public List<Company> getAll(){
		return companyService.getAllCmpany();
	}
	@PostMapping("add")
	public ResponseEntity<Void> add(@RequestBody Company company){
		companyService.addCompany(company);
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	@PutMapping("update")
	public RespBean update(@RequestBody Company company) {
		try {
			companyService.updateCompany(company);
			return new RespBean("200","更新成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
}
