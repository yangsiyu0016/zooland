package com.zoo.controller.erp.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.erp.product.Unit;
import com.zoo.service.erp.product.UnitService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/product/unit")
public class UnitController {
	@Autowired
	UnitService unitService;
	@GetMapping("list")
	public List<Unit> list(){
		return unitService.getUnitList();
	}
	
	@PostMapping("addUnit")
	public RespBean addUnit(@RequestBody Unit unit) {
		try {
			unitService.addUnit(unit);
			return new RespBean("200","添加成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
}
