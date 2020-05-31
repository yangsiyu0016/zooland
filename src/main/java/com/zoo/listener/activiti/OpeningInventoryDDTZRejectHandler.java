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
import com.zoo.controller.erp.constant.OpeningInventoryStatus;
import com.zoo.model.erp.inventorycheck.InventoryCheck;
import com.zoo.model.erp.openingInventory.OpeningInventory;
import com.zoo.service.erp.inventorycheck.InventoryCheckService;
import com.zoo.service.erp.openingInventory.OpeningInventoryService;
import com.zoo.utils.ApplicationUtil;

/**
 * 订单调整
 * @author aa
 *
 */
@Component("OpeningInventoryDDTZRejectHandler")
public class OpeningInventoryDDTZRejectHandler implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ProcessEngine processEngine;
	
	private OpeningInventoryService oiService;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		processEngine = (ProcessEngine) ApplicationUtil.getBean("processEngine");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		oiService = (OpeningInventoryService)ApplicationUtil.getBean("openingInventoryService");
		
		ProcessInstance result = runtimeService.createProcessInstanceQuery().processInstanceId(delegateTask.getProcessInstanceId()).singleResult();
		String key = result.getBusinessKey();
		OpeningInventory openingInventory = oiService.getOpeningInventoryById(key);
		//delegateTask.setAssignee(ic.getCuserId());
		Map<String,Object> condition = new HashMap<String,Object>();
		
		delegateTask.addCandidateUser(openingInventory.getCuserId());
		condition = new HashMap<String,Object>();
		condition.put("id", openingInventory.getId());
		condition.put("status", OpeningInventoryStatus.REJECT);
		oiService.updateOpeningInventoryStatus(condition);
	}

}
