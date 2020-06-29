package com.zoo.listener.activiti.purchase;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import com.zoo.controller.erp.constant.PurchaseStatus;
import com.zoo.model.erp.purchase.Purchase;
import com.zoo.service.erp.purchase.PurchaseService;
import com.zoo.utils.ApplicationUtil;

/**
 *财务审核通过
 * @author aa
 *
 */
@Component("PurchaseToInHandler")
public class PurchaseToInHandler implements ExecutionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8484762920218519501L;

	private ProcessEngine processEngine;
	
	private PurchaseService purchaseService;
	
	@Override
	public void notify(DelegateExecution execution) {
		// TODO Auto-generated method stub
		processEngine = (ProcessEngine) ApplicationUtil.getBean("processEngine");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		purchaseService = (PurchaseService) ApplicationUtil.getBean("purchaseService");
		
		ProcessInstance result = runtimeService.createProcessInstanceQuery().processInstanceId(execution.getProcessInstanceId()).singleResult();
		String key = result.getBusinessKey();
		Purchase purchase = purchaseService.getPurchaseById(key);
		Map<String,Object> condition = new HashMap<String,Object>();
		condition = new HashMap<String,Object>();
		condition.put("id", purchase.getId());
		condition.put("status", PurchaseStatus.IN);
		
		purchaseService.updatePurchaseStatus(condition);
	}

}
