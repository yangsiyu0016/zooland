package com.zoo.controller.erp.productsplit;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.exception.ZooException;
import com.zoo.model.erp.inbound.Inbound;
import com.zoo.model.erp.outbound.Outbound;
import com.zoo.model.erp.productsplit.ProductSplit;
import com.zoo.service.erp.productsplit.ProductSplitService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/erp/split")
public class ProductSplitController {

	@Autowired
	ProductSplitService splitService;
	
	@GetMapping("page")
	public Map<String, Object> page(@RequestParam("page") Integer page,
									@RequestParam("size") Integer size,
									@RequestParam("keywords") String keywords,
									@RequestParam("code") String code,
									@RequestParam("productCode") String productCode,
									@RequestParam("productName") String productName,
									@RequestParam("status") String status,
									@RequestParam("warehouseId") String warehouseId,
									@RequestParam("start_splitTime") String start_splitTime,
									@RequestParam("end_splitTime") String end_splitTime,
									@RequestParam("start_ctime") String start_ctime,
									@RequestParam("end_ctime") String end_ctime,
									@RequestParam("sort") String sort,
									@RequestParam("order") String order) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<ProductSplit> list = splitService.getProductSplitByPage(page, size, keywords, code, productCode,
				productName, status, warehouseId, start_splitTime, end_splitTime, start_ctime, end_ctime, sort, order);
		Long count = splitService.getCount(keywords, code, productCode, productName, status, warehouseId,
				start_splitTime, end_splitTime, start_ctime, end_ctime, sort, order);
		map.put("productSplits", list);
		map.put("count", count);
		
		return map;
	}
	
	@GetMapping("getProductSplitById")
	public ProductSplit getProductSplitById(@RequestParam("id") String id) {
		return splitService.getProductSplitById(id);
	}
	
	@GetMapping("updateNotOutNumberById")
	public RespBean updateNotOutNumberById(@RequestParam("notOutNumber") BigDecimal notOutNumber, @RequestParam("id") String id) {
		try {
			splitService.updateNotOutNumberById(notOutNumber, id);
			return new RespBean("200", "更新成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@PostMapping("addOutbound")
	public RespBean addOutbound(@RequestBody Outbound outbound) {
		try {
			splitService.addOutbound(outbound);
			return new RespBean("200", "添加成功");
		} catch (ZooException e) {
			// TODO: handle exception
			return new RespBean("500", e.getMsg());
		}
	}
	@DeleteMapping("deleteOut")
	public Map<String,Object> deleteOut(@RequestParam("splitId")String splitId,@RequestParam("outboundDetailId")String outboundDetailId,@RequestParam("type")String type){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			BigDecimal notOutNumber = splitService.deleteOut(splitId, outboundDetailId,type);
			resultMap.put("status", "200");
			resultMap.put("notOutNumber", notOutNumber);
		} catch (ZooException e) {
			resultMap.put("status", "500");
			resultMap.put("msg", e.getMsg());
		}
		return resultMap;
	}
	@PostMapping("addInbound")
	public RespBean addInbound(@RequestBody Inbound inbound) {
		try {
			splitService.addInbound(inbound);
			return new RespBean("200", "添加成功");
		} catch (ZooException e) {
			// TODO: handle exception
			return new RespBean("500", e.getMsg());
		}
	}
	@PostMapping("add")
	public RespBean add(@RequestBody ProductSplit split) {
		try {
			splitService.addProductSplit(split);
			return new RespBean("200","保存成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@PostMapping("update")
	public RespBean update(@RequestBody ProductSplit split) {
		 try {
			splitService.updatePeoductSplit(split);
			return new RespBean("200","更新成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@GetMapping("startFlow")
	public RespBean startFlow(@RequestParam("id") String id) {
		try {
			splitService.startProcess(id);
			return new RespBean("200", "启动成功");
		} catch (ZooException e) {
			// TODO: handle exception
			return new RespBean("500", e.getMsg());
		}
	}
	
	@GetMapping("destroy")
	public RespBean destroy(@RequestParam("id") String id) {
		try {
			splitService.destroy(id);
			return new RespBean("200", "作废成功");
		} catch (ZooException e) {
			// TODO: handle exception
			return new RespBean("500", e.getMsg());
		}
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "{ids}",method=RequestMethod.DELETE)
	public RespBean delete(@PathVariable String ids) {
		try {
			splitService.deleteDetailById(ids);
			return new RespBean("200", "删除成功");
		} catch (ZooException e) {
			// TODO: handle exception
			return new RespBean("500", e.getMsg());
		}
	}
	
	//取回
	@GetMapping("reset")
	public RespBean reject(@RequestParam("id") String id) {
		try {
			splitService.reset(id);
			return new RespBean("200", "取回成功");
		} catch (ZooException e) {
			// TODO: handle exception
			return new RespBean("500", e.getMsg());
		}
	}
	
}
