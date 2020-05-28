package com.zoo.listener.activiti;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import com.zoo.controller.erp.constant.InventoryCheckStatus;
import com.zoo.model.erp.inventorycheck.InventoryCheck;
import com.zoo.service.erp.inventorycheck.InventoryCheckService;
import com.zoo.utils.ApplicationUtil;

/**
 * 驳回重新调整完成重新提交
 * @author aa
 *
 */
@Component("InventoryCheckGXKCRejectComplete")
public class InventoryCheckGXKCRejectComplete implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1641775767438185083L;

	private ProcessEngine processEngine;
	private InventoryCheckService inventoryCheckService;
	
	
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		processEngine = (ProcessEngine) ApplicationUtil.getBean("processEngine");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		inventoryCheckService = (InventoryCheckService)ApplicationUtil.getBean("inventoryCheckService");
		
		ProcessInstance result = runtimeService.createProcessInstanceQuery().processInstanceId(delegateTask.getProcessInstanceId()).singleResult();
		String key = result.getBusinessKey();
		InventoryCheck ic = inventoryCheckService.getInventoryCheckById(key);
		
		Map<String,Object> condition = new HashMap<String,Object>();
		
		
		condition = new HashMap<String,Object>();
		condition.put("id", ic.getId());
		condition.put("status", InventoryCheckStatus.ZGSH);
		
		inventoryCheckService.updateInventoryCheckStatus(condition);
	}

}
