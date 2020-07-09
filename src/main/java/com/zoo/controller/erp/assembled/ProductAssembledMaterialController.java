package com.zoo.controller.erp.assembled;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.exception.ZooException;
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
	public RespBean addOutbound(@RequestBody Outbound outbound) {
		try {
			materialService.addOutbound(outbound);
			return new RespBean("200", "添加成功");
		} catch (Exception e) {
			// TODO: handle exception
			return new RespBean("500", e.getMessage());
		}
	}
	
	@DeleteMapping("deleteOut")
	public Map<String,Object> deleteOut(@RequestParam("assembledId")String assembledId,@RequestParam("outboundDetailId")String outboundDetailId,@RequestParam("type")String type){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			BigDecimal notOutNumber = materialService.deleteOut(assembledId, outboundDetailId,type);
			resultMap.put("status", "200");
			resultMap.put("notOutNumber", notOutNumber);
		} catch (ZooException e) {
			resultMap.put("status", "500");
			resultMap.put("msg", e.getMsg());
		}
		return resultMap;
	}
}
