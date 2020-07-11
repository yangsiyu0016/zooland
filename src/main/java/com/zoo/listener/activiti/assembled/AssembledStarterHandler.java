package com.zoo.listener.activiti.assembled;



import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import com.zoo.controller.erp.constant.ProductAssembledStatus;
import com.zoo.model.erp.assembled.ProductAssembled;
import com.zoo.service.erp.assembled.ProductAssembledService;
import com.zoo.utils.ApplicationUtil;
/**
 * wo Warehouse Operator 库管
 * @author 52547
 *
 */
@Component("assembledStarterHandler")
public class AssembledStarterHandler implements TaskListener{
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
		
		ProcessInstance result = runtimeService.createProcessInstanceQuery().processInstanceId(delegateTask.getProcessInstanceId()).singleResult();
		
		String key = result.getBusinessKey();
		
		ProductAssembled assembled = productAssembledService.getProductAssembledById(key);
		Map<String,Object> condition = new HashMap<String,Object>();
		delegateTask.addCandidateUser(assembled.getCuserId());
		condition = new HashMap<String,Object>();
		if("ZGSH".equals(assembled.getStatus())) {
			condition.put("id", assembled.getId());
			condition.put("status", ProductAssembledStatus.DDTZ);
		}else if ("CKLL".equals(assembled.getStatus())) {
			condition.put("id", assembled.getId());
			condition.put("status", ProductAssembledStatus.ASSEMBL);
		}
		productAssembledService.updateProductAssembledStatus(condition);
	}

}
