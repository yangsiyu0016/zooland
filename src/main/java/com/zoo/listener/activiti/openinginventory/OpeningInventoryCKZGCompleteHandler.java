package com.zoo.listener.activiti.openinginventory;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import com.zoo.controller.erp.constant.OpeningInventoryStatus;
import com.zoo.model.erp.openingInventory.OpeningInventory;
import com.zoo.service.erp.openingInventory.OpeningInventoryService;
import com.zoo.utils.ApplicationUtil;
/**
 * 仓库主管完成
 * @author 52547
 *
 */
@Component("openingInventoryCKZGCompleteHandler")
public class OpeningInventoryCKZGCompleteHandler implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8971579529870256870L;
	private ProcessEngine processEngine;
	private OpeningInventoryService openingInventoryService;
	@Override
	public void notify(DelegateTask delegateTask) {
		processEngine = (ProcessEngine) ApplicationUtil.getBean("processEngine");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		openingInventoryService = (OpeningInventoryService) ApplicationUtil.getBean("openingInventoryService");
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(delegateTask.getProcessInstanceId()).singleResult();
		String key = pi.getBusinessKey();
		OpeningInventory openingInventory= openingInventoryService.getOpeningInventoryById(key);
		Map<String,Object> condition = new HashMap<String,Object>();
		
	
		condition = new HashMap<String,Object>();
		condition.put("id", openingInventory.getId());
		condition.put("status", OpeningInventoryStatus.CWSH);
		//condition.put("etime", new Date());
		openingInventoryService.updateOpeningInventoryStatus(condition);
	}

}
