package com.zoo.controller.erp.aftersales.replenishmentnote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.erp.aftersales.replenishmentnote.ReplenishmentNoteDetail;
import com.zoo.service.erp.aftersales.replenishmentnote.ReplenishmentNoteDetailService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/erp/replenismentNote/detail")
public class ReplenishmentNoteDetailController {

	@Autowired
	ReplenishmentNoteDetailService detailService;
	
	@RequestMapping("getDetailByReplenismentNoteId")
	public List<ReplenishmentNoteDetail> getDetailByReplenismentNoteId(@RequestParam("replenishmentNoteId") String replenishmentNoteId) {
		return detailService.getDetailByReplenishmentNoteId(replenishmentNoteId);
	}
	
	@PostMapping("add")
	public Map<String, Object> addDetail(@RequestBody ReplenishmentNoteDetail detail) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			detail = detailService.addDatail(detail);
			map.put("detail", detail);
			map.put("status", 200);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("status", 500);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	
	@PutMapping("update")
	public RespBean updateDetail(@RequestBody ReplenishmentNoteDetail detail) {
		try {
			detailService.updateDetail(detail);
			return new RespBean("200", "更新成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@RequestMapping(value = "{ids}", method = RequestMethod.DELETE)
	public RespBean deleteDetail(@PathVariable String ids) {
		try {
			detailService.deleteDetailById(ids);
			return new RespBean("200", "删除成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
}
