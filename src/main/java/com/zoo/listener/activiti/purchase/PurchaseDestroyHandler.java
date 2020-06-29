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
import com.zoo.service.erp.purchase.PurchaseService;
import com.zoo.utils.ApplicationUtil;

/**
 * 订单调整完成
 * @author aa
 *
 */@Component("purchaseDestroyCompleteHandler")
public class PurchaseDestroyHandler implements ExecutionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2754046546477315871L;

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
		
		
		Map<String,Object> condition = new HashMap<String,Object>();
		
		//更新sell表中下一节点办理人
		condition = new HashMap<String,Object>();
		condition.put("id", key);
		condition.put("status", PurchaseStatus.DESTROY);
		
		purchaseService.updatePurchaseStatus(condition);
	}

}
