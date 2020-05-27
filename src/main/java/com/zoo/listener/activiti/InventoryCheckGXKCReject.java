package com.zoo.listener.activiti;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import com.zoo.controller.erp.constant.InventoryCheckStatus;
import com.zoo.model.erp.inventorycheck.InventoryCheck;
import com.zoo.service.erp.inventorycheck.InventoryCheckService;
import com.zoo.utils.ApplicationUtil;

@Component("InventoryCheckGXKCReject")
public class InventoryCheckGXKCReject implements TaskListener{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ProcessEngine processEngine;
	
	
	private InventoryCheckService inventoryCheckService;

	@Override
	public void notify(DelegateTask delegateTask) {
			processEngine = (ProcessEngine) ApplicationUtil.getBean("processEngine");
			RuntimeService runtimeService = processEngine.getRuntimeService();
			inventoryCheckService = (InventoryCheckService)ApplicationUtil.getBean("inventoryCheckService");
			
			ProcessInstance result = runtimeService.createProcessInstanceQuery().processInstanceId(delegateTask.getProcessInstanceId()).singleResult();
			String key = result.getBusinessKey();
			InventoryCheck ic = inventoryCheckService.getInventoryCheckById(key);
			//delegateTask.setAssignee(ic.getCuserId());
			Map<String,Object> condition = new HashMap<String,Object>();
			
			delegateTask.addCandidateUser(ic.getCuserId());
			condition = new HashMap<String,Object>();
			condition.put("id", ic.getId());
			condition.put("status", InventoryCheckStatus.REJECT);
			
			inventoryCheckService.updateInventoryCheckStatus(condition);
	}
	
	

	
	
}
