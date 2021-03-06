package com.zoo.controller.erp.aftersales.replenishmentnote;

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

import com.zoo.model.erp.aftersales.replenishmentnote.ReplenishmentNote;
import com.zoo.service.erp.aftersales.replenishmentnote.ReplenishmentNoteService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/erp/replenishmentNote")
public class ReplenishmentNoteController {

	@Autowired
	ReplenishmentNoteService replenishmentNoteService;
	
	@GetMapping("page")
	public Map<String, Object> page(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<ReplenishmentNote> list = replenishmentNoteService.getReplenishmentNote(page, size);
		Long count = replenishmentNoteService.getCount();
		map.put("replenishmentNotes", list);
		map.put("count", count);
		
		return map;
		
	}
	
	@PostMapping("addReplenishmentNote")
	public RespBean addReplenishmentNote(@RequestBody ReplenishmentNote note) {
		try {
			replenishmentNoteService.addReplenishmentNote(note);
			return new RespBean("200", "添加成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
		
	}
	
	@PutMapping("updateReplenishmentNote")
	public RespBean updateReplenishmentNote(@RequestBody ReplenishmentNote note) {
		try {
			replenishmentNoteService.updateReplenishmentNote(note);
			return new RespBean("200", "更新成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@RequestMapping(value = "{ids}", method = RequestMethod.DELETE)
	public RespBean deleteReplenishmentNoteById(@PathVariable String ids) {
		try {
			return new RespBean("200", "删除成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@GetMapping("startFlow")
	public RespBean startFlow(@RequestParam("id") String id) {
		try {
			replenishmentNoteService.startProcess(id);
			return new RespBean("200", "启动成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@GetMapping("destroy")
	public RespBean destroy(@RequestParam("id") String id) {
		try {
			replenishmentNoteService.destroy(id);
			return new RespBean("200", "作废成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public ReplenishmentNote getReplenishmentNoteId(@PathVariable String id) {
		return replenishmentNoteService.getReplenishmentNoteById(id);
	}
}
