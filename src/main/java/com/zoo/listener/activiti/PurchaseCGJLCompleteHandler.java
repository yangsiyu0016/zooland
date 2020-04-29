package com.zoo.listener.activiti;


import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import com.zoo.controller.erp.constant.PurchaseStatus;
import com.zoo.service.erp.purchase.PurchaseService;

import com.zoo.utils.ApplicationUtil;
/**
 * 采购经理完成
 * @author 52547
 *
 */
@Component("purchaseCGJLCompleteHandler")
public class PurchaseCGJLCompleteHandler implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ProcessEngine processEngine;
	private PurchaseService purchaseService;
	@Override
	public void notify(DelegateTask delegateTask) {
		processEngine=(ProcessEngine) ApplicationUtil.getBean("processEngine");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		purchaseService = (PurchaseService) ApplicationUtil.getBean("purchaseService");
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(delegateTask.getProcessInstanceId()).singleResult();
		String key = pi.getBusinessKey();
		
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("id", key);
		condition.put("status", PurchaseStatus.CWSH);
		//condition.put("cwtgtime", new Date());
		purchaseService.updatePurchaseStatus(condition);
	}
	
}
