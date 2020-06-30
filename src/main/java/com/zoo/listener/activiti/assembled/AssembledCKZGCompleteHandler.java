package com.zoo.listener.activiti.assembled;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import com.zoo.controller.erp.constant.ProductAssembledStatus;
import com.zoo.model.erp.assembled.ProductAssembled;
import com.zoo.model.erp.warehouse.Warehouse;
import com.zoo.service.erp.assembled.ProductAssembledService;
import com.zoo.service.erp.productsplit.ProductSplitService;
import com.zoo.utils.ApplicationUtil;

/**
 * 组装单仓库主管完成
 * @author aa
 *
 */
@Component("AssembledCKZGCompleteHandler")
public class AssembledCKZGCompleteHandler implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -412995073407848259L;

	private ProcessEngine processEngine;
	private ProductAssembledService productAssembledService;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		processEngine = (ProcessEngine) ApplicationUtil.getBean("processEngine");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		
		productAssembledService = (ProductAssembledService) ApplicationUtil.getBean("productAssembledService");
		ProcessInstance result = runtimeService.createProcessInstanceQuery().processInstanceId(delegateTask.getProcessInstanceId()).singleResult();
		
		String key = result.getBusinessKey();
		
		ProductAssembled assembled = productAssembledService.getProductAssembledById(key);
		List<String> warehouseIdList = new ArrayList<String>();
		Warehouse warehouse = assembled.getWarehouse();
		
		Map<String, Object> variables = delegateTask.getVariables();
		warehouseIdList.add(warehouse.getId());
		variables.put("warehouseIds", warehouseIdList);
		delegateTask.setVariables(variables);
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("id", assembled.getId());
		condition.put("status", ProductAssembledStatus.CKLL);
		productAssembledService.updateProductAssembledStatus(condition);
	}

}
