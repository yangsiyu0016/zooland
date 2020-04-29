package com.zoo.controller.crm.customer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.crm.Linkman;
import com.zoo.service.crm.customer.LinkmanService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/customer/linkman")
public class LinkmanController {
	@Autowired
	LinkmanService linkmanService;
	@PostMapping("addLinkman")
	public Map<String,Object> addLinkman(@RequestBody Linkman linkman) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			linkman = linkmanService.addLinkman(linkman);
			map.put("status", 200);
			map.put("linkman", linkman);
		} catch (Exception e) {
			map.put("status", 500);
			map.put("msg", e.getMessage());
			
		}
		return map;
	}
	@PutMapping("updateLinkman")
	public RespBean updateLinkman(@RequestBody Linkman linkman) {
		try {
			linkmanService.updateLinkman(linkman);
			return new RespBean("200","更新成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
}
