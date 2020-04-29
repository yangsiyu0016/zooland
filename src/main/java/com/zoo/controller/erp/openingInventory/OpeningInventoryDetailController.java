package com.zoo.controller.erp.openingInventory;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.erp.openingInventory.OpeningInventoryDetail;
import com.zoo.service.erp.openingInventory.OpeningInventoryDetailService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/oi/detail")
public class OpeningInventoryDetailController {
	@Autowired
	OpeningInventoryDetailService detailService;
	/**
	 * 添加期初单产品
	 * @param detail
	 * @return
	 */
	@PostMapping("add")
	public Map<String,Object> addDetail(@RequestBody OpeningInventoryDetail detail) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			detail = detailService.addDetail(detail);
			resultMap.put("status", 200);
			resultMap.put("detail", detail);
		} catch (Exception e) {
			resultMap.put("status", 500);
			resultMap.put("msg", e.getMessage());
		}
		return resultMap;
	}
	@PutMapping("updatePrice")
	public void updatePrice(@RequestParam("id")String id,@RequestParam("costPrice") String costPrice,@RequestParam("totalMoney")String totalMoney) {
		detailService.updatePrice(id,costPrice,totalMoney);
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
