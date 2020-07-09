package com.zoo.controller.erp.productsplit;

import java.util.HashMap;
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

import com.zoo.exception.ZooException;
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
	@PostMapping("inbound")
	public RespBean inbound (@RequestParam("id")String id) {
		try {
			detailService.inbound(id);
			return new RespBean("200","操作成功");
		} catch (ZooException e) {
			return new RespBean("500",e.getMsg());
		}
	}
	@PostMapping("cancelInbound")
	public RespBean cancelInbound (@RequestParam("id")String id) {
		try {
			detailService.cancelInbound(id);
			return new RespBean("200","操作成功");
		} catch (ZooException e) {
			return new RespBean("500",e.getMsg());
		}
	}
}
