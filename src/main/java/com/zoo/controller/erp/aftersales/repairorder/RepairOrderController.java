package com.zoo.controller.erp.aftersales.repairorder;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.erp.aftersales.repairorder.RepairOrder;
import com.zoo.service.erp.aftersales.repairorder.RepairOrderService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/erp/repairOrder")
public class RepairOrderController {

	@Autowired
	RepairOrderService repairOrderService;
	
	@GetMapping("page")
	public Map<String, Object> page(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<RepairOrder> list = repairOrderService.getRepairOrderByPage(page, size);
		Long count = repairOrderService.getCount();
		map.put("repairOrders", list);
		map.put("count", count);
		return map;
	}
	
	@PostMapping("addRepairOrder")
	public RespBean add(@RequestBody RepairOrder order) {
		try {
			repairOrderService.addRepairOrder(order);
			return new RespBean("200", "添加成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@PutMapping("updateRepairOrder")
	public RespBean updateRepairOrder(@RequestBody RepairOrder order) {
		try {
			repairOrderService.updateRepairOrder(order);
			return new RespBean("200", "更新成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@RequestMapping(value = "{ids}", method = RequestMethod.DELETE)
	public RespBean deleteRepairOrderById(@PathVariable String ids) {
		try {
			return new RespBean("200", "删除成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@GetMapping("startFlow")
	public RespBean startFlow(@RequestParam("id") String id) {
		try {
			repairOrderService.startProcess(id);
			return new RespBean("200", "启动成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@GetMapping("destroy")
	public RespBean destroy(@RequestParam("id") String id) {
		try {
			repairOrderService.destroy(id);
			return new RespBean("200", "作废成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public RepairOrder getRepairOrderById(@PathVariable String id) {
		return repairOrderService.getRepairOrderById(id);
	}
}
