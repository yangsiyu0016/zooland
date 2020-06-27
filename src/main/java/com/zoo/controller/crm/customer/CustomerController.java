package com.zoo.controller.crm.customer;

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

import com.zoo.model.crm.Customer;
import com.zoo.service.crm.customer.CustoemrService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/crm/customer")
public class CustomerController {
	@Autowired
	CustoemrService customerService;
	@GetMapping("page")
	public Map<String,Object> page(
			@RequestParam("page")Integer page,
			@RequestParam("size")Integer size,
			@RequestParam("keywords")String keywords,
			@RequestParam("customerName")String customerName,
			@RequestParam("linkman")String linkman,
			@RequestParam("owner")String owner,
			@RequestParam("start_gtime")String start_gtime,
			@RequestParam("end_gtime")String end_gtime,
			@RequestParam("start_created")String start_created,
			@RequestParam("end_created")String end_created,
			@RequestParam("sort")String sort,
			@RequestParam("order")String order){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Customer> customers = customerService.getCustomerByPage(page,size,keywords,customerName,linkman,owner,start_gtime,end_gtime,start_created,end_created,sort,order);
		long count = customerService.getCount(keywords,customerName,linkman,owner,start_gtime,end_gtime,start_created,end_created);
		map.put("customers", customers);
		map.put("count", count);
		return map;
	}
	@PostMapping("addCustomer")
	public RespBean addCustomer(@RequestBody Customer customer) {
		try {
			customerService.addCustomer(customer);
			return new RespBean("200","添加成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
	@PutMapping("updateCustomer")
	public RespBean updateCustomer(@RequestBody Customer customer) {
		try {
			customerService.updateCustomer(customer);
			return new RespBean("200","更新成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
}
