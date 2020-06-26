package com.zoo.controller.erp.productsplit;

import java.math.BigDecimal;
import java.util.HashMap;
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

import com.zoo.model.erp.inbound.Inbound;
import com.zoo.model.erp.productsplit.ProductSplitDetail;
import com.zoo.service.erp.productsplit.ProductSplitDetailService;
import com.zoo.vo.RespBean;


@RestController
@RequestMapping("/erp/splitDetail")
public class ProductSplitDetailController {

	@Autowired
	ProductSplitDetailService detailService;
	
	/**
	 * 添加
	 * @param detail
	 * @return
	 */
	@PostMapping("add")
	public Map<String, Object> addDetail(@RequestBody ProductSplitDetail detail ) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			detail = detailService.addDetail(detail);
			map.put("status", 200);
			map.put("detail", detail);
		} catch (Exception e) {
			// TODO: handle exception
			map.put("status", 500);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	
	/**
	 * 更新
	 * @param detail
	 * @return
	 */
	@PutMapping("update")
	public RespBean update(@RequestBody ProductSplitDetail detail) {
		try {
			detailService.updateDetail(detail);
			return new RespBean("200", "更新成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@GetMapping("updateNotInNumberById")
	public RespBean updateNotInNumberById(@RequestParam("notInNumber") BigDecimal notInNumber, @RequestParam("id") String id) {
		try {
			detailService.updateNotInNumberById(notInNumber, id);
			return new RespBean("200", "更新成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "ids", method = RequestMethod.DELETE)
	public RespBean delete(@PathVariable String ids) {
		try {
			detailService.deleteDetailById(ids);
			return new RespBean("200", "删除成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@PostMapping("addInbound")
	public RespBean addInbound(@RequestBody Inbound inbound, @RequestParam("number") BigDecimal number, @RequestParam("goodsAllocationId") String goodsAllocationId) {
		try {
			detailService.addInbound(inbound, goodsAllocationId, number);
			return new RespBean("200", "添加成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
}
