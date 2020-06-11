package com.zoo.controller.system.parameter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.system.parameter.SystemParameterDirectory;
import com.zoo.service.system.parameter.SystemParameterDirectoryService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/system/parameterDirectory")
public class SystemParameterDirectoryController {
	@Autowired
	SystemParameterDirectoryService directoryService;
	@GetMapping("tree")
	public List<SystemParameterDirectory> getTreeData(){
		return directoryService.getTreeData();
	}
	@GetMapping("list")
	public List<SystemParameterDirectory> getDirectoryList(){
		return directoryService.getDirectoryList();
	}
	@RequestMapping("addDirectory")
	public RespBean addDirectory(@RequestBody SystemParameterDirectory directory) {
		try {
			directoryService.addDirectory(directory);
			return new RespBean("200","添加成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
}
