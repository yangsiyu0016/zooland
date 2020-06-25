package com.zoo.listener.activiti.openinginventory;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;

import com.zoo.controller.erp.constant.OpeningInventoryStatus;
import com.zoo.model.erp.openingInventory.OpeningInventory;
import com.zoo.service.erp.openingInventory.OpeningInventoryService;
import com.zoo.utils.ApplicationUtil;

/**
 * 订单调整完成
 * @author aa
 *
 */
public class OpeningInventoryDDTZRejectCompleteHandler implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3976528369091148869L;

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
		
		condition = new HashMap<String,Object>();
		condition.put("id", openingInventory.getId());
		condition.put("status", OpeningInventoryStatus.ZGSH);
		oiService.updateOpeningInventoryStatus(condition);
	}

}
