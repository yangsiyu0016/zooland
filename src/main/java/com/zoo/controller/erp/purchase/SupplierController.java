package com.zoo.controller.erp.purchase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.erp.purchase.Supplier;
import com.zoo.service.erp.purchase.SupplierService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/erp/supplier")
public class SupplierController {
	
	//引入逻辑层
	@Autowired
	private SupplierService supplierService;
	
	//供货商管理-分页查询数据
	@GetMapping("page")
	public Map<String, Object> page(@RequestParam("page")Integer page,
			@RequestParam("size")Integer size,
			@RequestParam("keywords")String keywords,
			@RequestParam("supplierName")String supplierName,
			@RequestParam("linkman")String linkman,
			@RequestParam("owner")String owner,
			@RequestParam("start_gtime")String start_gtime,
			@RequestParam("end_gtime")String end_gtime,
			@RequestParam("start_createTime")String start_createTime,
			@RequestParam("end_createTime")String end_createTime,
			@RequestParam("sort")String sort,
			@RequestParam("order")String order) {
		Map<String,Object> map = new HashMap<String, Object>();
		
		List<Supplier> suppliers = supplierService.getSupplierByPage(page,size,keywords,supplierName,linkman,owner,start_gtime,end_gtime,start_createTime,end_createTime,sort,order);
		
		long count = supplierService.getCount(keywords,supplierName,linkman,owner,start_gtime,end_gtime,start_createTime,end_createTime);
		
		map.put("suppliers", suppliers);
		
		map.put("count", count);
		
		return map;
		
	}
	
	//供货商管理-新增数据
	@PostMapping("add")
	public RespBean add(@RequestBody Supplier supplier){
		
		try {
			
			supplierService.add(supplier);
			
			return new RespBean("200", "保存成功");
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
			return new RespBean("500", "保存失败");
		}
	}
	
	//供货商管理-修改数据
	@PutMapping("update")
	public RespBean update(@RequestBody Supplier supplier) {
		
		try {
			supplierService.update(supplier);
			return new RespBean("200", "修改成功");
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
			return new RespBean("500", "修改失败");
		}
		
	}
}
