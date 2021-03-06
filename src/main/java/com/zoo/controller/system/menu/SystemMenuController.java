package com.zoo.controller.system.menu;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.exception.ZooException;
import com.zoo.model.system.menu.SystemMenu;
import com.zoo.service.system.menu.SystemMenuService;
import com.zoo.vo.RespBean;
@RestController
@RequestMapping("/menu")
public class SystemMenuController {
	@Autowired
	private SystemMenuService systemMenuService;
	@GetMapping("listMenuByUser")
	public ResponseEntity<List<SystemMenu>> listMenuByUser(){
		List<SystemMenu> menus = new ArrayList<SystemMenu>();
		SystemMenu menu = systemMenuService.getMenuById("86280221-52a6-46b4-a7ea-57dc4d1ce9b7");
		menus.add(menu);
		return ResponseEntity.ok(menus);
	}
	@GetMapping("getCompanyTreeData")
	public ResponseEntity<List<SystemMenu>> getCompanyTreeData(){
		List<SystemMenu> menus = systemMenuService.getCompanyMenuData();
		return ResponseEntity.ok(menus);
	}
	@GetMapping("getAllMenu")
	public ResponseEntity<List<SystemMenu>> menuTree(){
		List<SystemMenu> menus = systemMenuService.menuTree();
		return ResponseEntity.ok(menus);
	}
	@PostMapping("addMenu")
	public RespBean addMenu(@RequestBody SystemMenu menu){
		try {
			systemMenuService.addMenu(menu);
			return new RespBean("200","添加成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}

	}
	@PutMapping("update")
	public RespBean update(@RequestBody SystemMenu menu) {
		try {
			systemMenuService.updateMenu(menu);
			return new RespBean("200","更新成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
			
		
	}
	@DeleteMapping("/del/{id}")
	public RespBean delMenu(@PathVariable String id) {
		try {
			systemMenuService.deleteMenuById(id);
			return new RespBean("success","删除成功");
		} catch (ZooException e) {
			return new RespBean("error",e.getExceptionEnum().message());
		}
	}
}
