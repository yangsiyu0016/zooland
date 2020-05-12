package com.zoo.controller.system.base;

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

import com.zoo.model.system.base.Express;
import com.zoo.service.system.base.ExpressService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/system/express")
public class ExpressController {
	@Autowired
	ExpressService expressService;
	@GetMapping("page")
	public Map<String,Object> page(@RequestParam("page")Integer page,@RequestParam("size")Integer size){
		Map<String,Object> map = new HashMap<String,Object>();
		List<Express> expresses = expressService.getExpressByPage(page, size);
		long count = expressService.getCount();
		map.put("expresses", expresses);
		map.put("count", count);
		return map;
	}
	@GetMapping("all")
	public List<Express> all(){
		return expressService.getExpressByPage(null, null);
	}
	@GetMapping("getExpressById")
	public Express getExpressById(String id) {
		return expressService.getExpressById(id);
	}
	@PostMapping("addExpress")
	public RespBean addExpress(@RequestBody Express express) {
		try {
			expressService.addExpress(express);
			return new RespBean("200","添加成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
	@PutMapping("updateExpress")
	public RespBean updateExpress(@RequestBody Express express) {
		try {
			expressService.updateExpress(express);
			return new RespBean("200","更新成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
}
