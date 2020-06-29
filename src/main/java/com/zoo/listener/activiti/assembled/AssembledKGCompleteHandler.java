package com.zoo.listener.activiti.assembled;



import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import com.zoo.controller.erp.constant.ProductAssembledStatus;
import com.zoo.model.erp.assembled.ProductAssembled;
import com.zoo.model.erp.warehouse.Warehouse;
import com.zoo.model.system.user.SystemUser;
import com.zoo.service.erp.assembled.ProductAssembledService;
import com.zoo.service.erp.warehouse.WarehouseService;
import com.zoo.utils.ApplicationUtil;
/**
 * wo Warehouse Operator 库管
 * @author 52547
 *
 */
@Component("assembledKGCompleteHandler")
public class AssembledKGCompleteHandler implements TaskListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ProcessEngine processEngine;
	//private WarehouseService warehouseService;
	private ProductAssembledService productAssembledService;
	@Override
	public void notify(DelegateTask delegateTask) {
		processEngine = (ProcessEngine)ApplicationUtil.getBean("processEngine");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		productAssembledService = (ProductAssembledService)ApplicationUtil.getBean("productAssembledService");
		
		ProcessInstance result = runtimeService.createProcessInstanceQuery()
				.processInstanceId(delegateTask.getProcessInstanceId()).singleResult();
		String key = result.getBusinessKey();
		
		ProductAssembled assembled = productAssembledService.getProductAssembledById(key);
		
		// 更新订单状态
		Map<String, Object> condition = new HashMap<String, Object>();
		if(ProductAssembledStatus.ZGSH.equals(assembled.getStatus())) {
			condition.put("id", assembled.getId());
			condition.put("status", ProductAssembledStatus.ASSEMBL);
		}else if(ProductAssembledStatus.ASSEMBL.equals(assembled.getStatus())) {
			condition.put("id", assembled.getId());
			condition.put("status", ProductAssembledStatus.FINISHED);
			condition.put("etime", new Date());
		}
		productAssembledService.updateProductAssembledStatus(condition);
	}

}
