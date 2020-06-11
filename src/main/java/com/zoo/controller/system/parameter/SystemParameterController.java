package com.zoo.controller.system.parameter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.system.parameter.SystemParameter;
import com.zoo.service.system.parameter.SystemParameterService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/system/parameter")
public class SystemParameterController {
	@Autowired
	SystemParameterService parameterService;
	@GetMapping("list")
	public List<SystemParameter> list(){
		return parameterService.getParameterList();
	}
	
	@RequestMapping("addParameter")
	public RespBean addParameter(@RequestBody SystemParameter parameter) {
		try {
			parameterService.addParameter(parameter);
			return new RespBean("200","添加成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
}
