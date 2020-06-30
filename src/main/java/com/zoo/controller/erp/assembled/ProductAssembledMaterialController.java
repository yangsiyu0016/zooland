package com.zoo.controller.erp.assembled;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.model.erp.outbound.Outbound;
import com.zoo.service.erp.assembled.ProductAssembledMaterialService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/erp/assembledMaterial")
public class ProductAssembledMaterialController {

	@Autowired
	ProductAssembledMaterialService materialService;
	
	/**
	 * 更新未出库数量
	 * @param notOutNumber
	 * @param id
	 * @return
	 */
	@GetMapping("updateNotOutNumber")
	public RespBean updateNotOutNumber(@RequestParam("notOutNumber") BigDecimal notOutNumber, @RequestParam("id") String id) {
		try {
			materialService.updateNotOutNumber(notOutNumber, id);
			return new RespBean("200", "更新成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@PostMapping("addOutbound")
	public RespBean addOutbound(@RequestBody Outbound outbound, @RequestParam("number") BigDecimal number, @RequestParam("goodsAllocationId") String goodsAllocationId) {
		try {
			materialService.addOutbound(outbound, goodsAllocationId, number);
			return new RespBean("200", "添加成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@GetMapping("getOutboundByProductAssembledId")
	public Map<String, Object> getOutboundByProductAssembledId(@RequestParam("id") String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Outbound> list = materialService.getOutboundByProductAssembledId(id);
		map.put("status", 200);
		map.put("outbounds", list);
		return map;
	}
}
