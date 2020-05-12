package com.zoo.controller.erp.sell;

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

import com.zoo.exception.ZooException;
import com.zoo.model.erp.sell.Sell;
import com.zoo.service.erp.sell.SellService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/erp/sell")
public class SellController {
	@Autowired
	SellService sellService;
	@GetMapping("page")
	public Map<String,Object> page(@RequestParam("page")Integer page,@RequestParam("size")Integer size){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Sell> sells = sellService.getSellByPage(page,size);
		long count = sellService.getCount();
		map.put("sells", sells);
		map.put("count", count);
		return map;
	}
	@RequestMapping(value="{id}",method=RequestMethod.GET)
	public Sell getSellById(@PathVariable String id) {
		return sellService.getSellById(id);
	}
	@PostMapping("add")
	public RespBean addSell(@RequestBody Sell sell) {
		try {
			sellService.addSell(sell);
			return new RespBean("200","添加成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
	@PutMapping("update")
	public RespBean updateSell(@RequestBody Sell sell) {
		try {
			sellService.updateSell(sell);
			return new RespBean("200","更新成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
	@RequestMapping(value = "{ids}",method=RequestMethod.DELETE)
	public RespBean deleteSellById(@PathVariable String ids) {
		try {
			sellService.deleteSellById(ids);
			return new RespBean("200","删除成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
	@PostMapping("startFlow")
	public RespBean startFlow(@RequestParam("id")String id) {
		try {
			sellService.startProcess(id);
			return new RespBean("200","启动成功");
		} catch (ZooException e) {
			return new RespBean("500",e.getExceptionEnum().message());
		}
	}
	
	//作废订单
	@PostMapping("destroy")
	public RespBean destroy(@RequestParam("id") String id) {
		try {
			sellService.destroy(id);
			return new RespBean("200", "作废成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
}
