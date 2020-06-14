package com.zoo.controller.erp.aftersales.changeorder;

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

import com.zoo.model.erp.aftersales.changeorder.ChangeOrder;
import com.zoo.service.erp.aftersales.changeorder.ChangeOrderService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/erp/changeOrder")
public class ChangeOrderController {

	@Autowired
	ChangeOrderService changeOrderService;
	
	@GetMapping("page")
	public Map<String, Object> page(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ChangeOrder> list = changeOrderService.getChangeOrderByPage(page, size);
		Long count = changeOrderService.getCount();
		map.put("changeOrders", list);
		map.put("count", count);
		return map;
	}
	
	@PostMapping("addChangeOrder")
	public RespBean addChangeOrder(@RequestBody ChangeOrder order) {
		try {
			changeOrderService.addChangeOrder(order);
			return new RespBean("200", "添加成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@PutMapping("updateChangeOrder")
	public RespBean updateChangeOrder(@RequestBody ChangeOrder order) {
		try {
			changeOrderService.updateChangeOrder(order);
			return new RespBean("200", "更新成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@RequestMapping(value = "{ids}", method = RequestMethod.DELETE)
	public RespBean deleteChangeOrderById(@PathVariable String ids) {
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
			changeOrderService.startProcess(id);
			return new RespBean("200", "启动成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@GetMapping("destroy")
	public RespBean destroy(@RequestParam("id") String id) {
		try {
			changeOrderService.destroy(id);
			return new RespBean("200", "作废成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public ChangeOrder getChangeOrderById(@PathVariable String id) {
		return changeOrderService.getChangeOrderById(id);
	}
	
	
}
