package com.zoo.controller.erp.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.exception.ZooException;
import com.zoo.model.erp.product.MaterialStructure;
import com.zoo.service.erp.product.MaterialStructureService;
import com.zoo.vo.RespBean;
@RestController
@RequestMapping("/product/ms")
public class MaterialStructureController {
	@Autowired
	MaterialStructureService msService;
	@RequestMapping("add")
	public RespBean addMaterialStructure(@RequestBody MaterialStructure ms) {
		try {
			msService.addMaterialStructure(ms);
			return new RespBean("200","保存成功");
		} catch (ZooException e) {
			return new RespBean("500",e.getMsg());
		}
	}
}
