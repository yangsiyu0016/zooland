package com.zoo.controller.erp.assembled;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.exception.ZooException;
import com.zoo.model.erp.assembled.ProductAssembled;
import com.zoo.model.erp.inbound.Inbound;
import com.zoo.service.erp.assembled.ProductAssembledService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/erp/assembled")
public class ProductAssembledController {
	@Autowired
	ProductAssembledService paService;
	@GetMapping("page")
	public Map<String,Object> page(@RequestParam("page")Integer page,
									@RequestParam("size")Integer size,
									@RequestParam(value="keywords") String keywords,
									@RequestParam(value="code") String code,
									@RequestParam(value="productCode") String productCode,
									@RequestParam(value="productName") String productName,
									@RequestParam(value="status") String status,
									@RequestParam(value="warehouseId") String warehouseId,
									@RequestParam(value="start_assembledTime") String start_assembledTime,
									@RequestParam(value="end_assembledTime") String end_assembledTime,
									@RequestParam(value="start_ctime") String start_ctime,
									@RequestParam(value="end_ctime") String end_ctime,
									@RequestParam(value="sort") String sort,
									@RequestParam(value="order") String order){
		Map<String,Object> map = new HashMap<String,Object>();
		List<ProductAssembled> productAssembleds = paService.getProductAssembledByPage(page,size,keywords,
				code,productCode,productName,status,warehouseId,start_assembledTime,end_assembledTime,start_ctime,end_ctime,sort,order);
		long count = paService.getCount(keywords,code,productCode,productName,status,warehouseId,start_assembledTime,end_assembledTime,start_ctime,end_ctime);
		map.put("productAssembleds", productAssembleds);
		map.put("count", count);
		return map;
	}
	@RequestMapping(value="{id}",method=RequestMethod.GET)
	public ProductAssembled getProductAssembledById(@PathVariable String id) {
		return paService.getProductAssembledById(id);
	}
	@PostMapping("add")
	public RespBean addProductAssembled(@RequestBody ProductAssembled productAssembled) {
		try {
			paService.addProductAssembled(productAssembled);
			return new RespBean("200","添加成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
	@PutMapping("update")
	public RespBean updateProductAssembled(@RequestBody ProductAssembled productAssembled) {
		try {
			paService.updateProductAssembled(productAssembled);
			return new RespBean("200","更新成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
	@RequestMapping(value = "{ids}",method=RequestMethod.DELETE)
	public RespBean deleteProductAssembledById(@PathVariable String ids) {
		try {
			paService.deleteProductAssembledById(ids);
			return new RespBean("200","删除成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
	}
	@PostMapping("startFlow")
	public RespBean startFlow(@RequestParam("id")String id) {
		try {
			paService.startProcess(id);
			return new RespBean("200","启动成功");
		} catch (ZooException e) {
			return new RespBean("500",e.getExceptionEnum().message());
		}
	}
	//取回
	@GetMapping("takeBack")
	public RespBean takeBack(@RequestParam("id") String id) {
		try {
			paService.takeBack(id);
			return new RespBean("200", "驳回成功");
		} catch (ZooException e) {
			// TODO: handle exception
			return new RespBean("500", e.getMsg());
		}
	}
	@PostMapping("addInbound")
	public RespBean addInbound(@RequestBody Inbound inbound) {
		try {
			paService.addInbound(inbound);
			return new RespBean("200", "保存成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@GetMapping("inboundOperation")
	public RespBean inboundOperation(@RequestParam("id") String id) {
		try {
			paService.inboundOperation(id);
			return new RespBean("200", "入库成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@GetMapping("getAssembledById")
	public ProductAssembled getAssembledById(@RequestParam("id") String id) {
		return paService.getProductAssembledById(id);
	}
	
	@DeleteMapping("deleteIn")
	public Map<String, Object> deleteIn(@RequestParam("assembledId") String assembledId, @RequestParam("inboundDetailId") String inboundDetailId, @RequestParam("type") String type) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			BigDecimal notInNumber = paService.deleteIn(assembledId, inboundDetailId, type);
			resultMap.put("status", "200");
			resultMap.put("notInNumber", notInNumber);
		} catch (ZooException e) {
			// TODO: handle exception
			resultMap.put("status", "500");
			resultMap.put("msg", e.getMessage());
		}
		return resultMap;
	}
	
	@GetMapping("destroy")
	public RespBean destroy(@RequestParam("id") String id) {
		try {
			paService.destroy(id);
			return new RespBean("200", "作废成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		} 
	}
}
