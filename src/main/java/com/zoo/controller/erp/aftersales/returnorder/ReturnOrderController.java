package com.zoo.controller.erp.aftersales.returnorder;

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

import com.zoo.model.erp.aftersales.returnorder.ReturnOrder;
import com.zoo.service.erp.aftersales.returnorder.ReturnOrderService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/erp/returnOrder")
public class ReturnOrderController {

	@Autowired
	ReturnOrderService returnOrderService;
	
	@GetMapping("page")
	public Map<String, Object> page(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ReturnOrder> list = returnOrderService.getReturnOrderByPage(page, size);
		Long count = returnOrderService.getCount();
		map.put("returnOrders", list);
		map.put("count", count);
		return map;
	}
	
	@PostMapping("addReturnOrder")
	public RespBean addReturnOrder(@RequestBody ReturnOrder order) {
		try {
			returnOrderService.addReturnOrder(order);
			return new RespBean("200", "添加成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@PutMapping("updateReturnOrder")
	public RespBean updateReturnOrder(@RequestBody ReturnOrder order) {
		try {
			returnOrderService.updateReturnOrder(order);
			return new RespBean("200", "更新成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@RequestMapping(value = "{ids}", method = RequestMethod.DELETE)
	public RespBean deleteReturnOrderById(@PathVariable String ids) {
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
			returnOrderService.startProcess(id);
			return new RespBean("200", "开启成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@GetMapping("destroy")
	public RespBean destroy(@RequestParam("id") String id) {
		try {
			returnOrderService.destroy(id);
			return new RespBean("200", "作废成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public ReturnOrder getReturnOrderById(@PathVariable String id) {
		return returnOrderService.getReturnOrderById(id);
	}
	
}
