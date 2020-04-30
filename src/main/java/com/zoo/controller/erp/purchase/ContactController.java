package com.zoo.controller.erp.purchase;

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

import com.zoo.model.erp.purchase.Contact;
import com.zoo.service.erp.purchase.ContactService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/erp/contact")
public class ContactController {

	@Autowired
	private ContactService contactService;
	
	@PostMapping("addContact")
	public Map<String,Object> addContact(@RequestBody Contact contact) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			contact = contactService.addContact(contact);
			map.put("status", 200);
			map.put("contact", contact);
		} catch (Exception e) {
			map.put("status", 500);
			map.put("msg", e.getMessage());
			
		}
		return map;
	}
	@PutMapping("updateContact")
	public RespBean updateContact(@RequestBody Contact contact) {
		try {
			int updateLinkman = contactService.updateContact(contact);
			return new RespBean("200","更新成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
	
}
