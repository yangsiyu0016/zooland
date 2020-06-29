package com.zoo.listener.activiti.purchase;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.purchase.Purchase;
import com.zoo.model.erp.purchase.PurchaseDetail;
import com.zoo.service.erp.purchase.PurchaseService;

import com.zoo.utils.ApplicationUtil;
/**
 * 财务审核完成
 * @author 52547
 *
 */
@Component("purchaseCWCompleteHandler")
public class PurchaseCWCompleteHandler implements TaskListener {

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
		Purchase purchase= purchaseService.getPurchaseById(key);
		List<PurchaseDetail> details = purchase.getDetails();
		List<String> warehouseIdList = new ArrayList<String>();
		for(PurchaseDetail detail:details){
			if(!warehouseIdList.contains(detail.getWarehouse().getId())) warehouseIdList.add(detail.getWarehouse().getId());
		}
		Map<String, Object> variables = delegateTask.getVariables();
		 
		variables.put("warehouseIds", warehouseIdList);
		delegateTask.setVariables(variables);
	}
	
}
