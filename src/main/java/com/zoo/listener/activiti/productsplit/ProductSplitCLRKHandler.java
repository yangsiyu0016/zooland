package com.zoo.listener.activiti.productsplit;

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

import com.zoo.controller.erp.constant.ProductSplitStatus;
import com.zoo.model.erp.productsplit.ProductSplit;
import com.zoo.model.erp.warehouse.Warehouse;
import com.zoo.model.system.user.SystemUser;
import com.zoo.service.erp.productsplit.ProductSplitService;
import com.zoo.service.erp.warehouse.WarehouseService;
import com.zoo.utils.ApplicationUtil;

/**
 * 材料入库
 * @author aa
 *
 */
@Component("productSplitCLRKHandler")
public class ProductSplitCLRKHandler implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ProcessEngine processEngine;
	private WarehouseService warehouseService;
	private ProductSplitService productSplitService;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		processEngine = (ProcessEngine) ApplicationUtil.getBean("processEngine");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		
		productSplitService = (ProductSplitService) ApplicationUtil.getBean("productSplitService");
		warehouseService = (WarehouseService)ApplicationUtil.getBean("warehouseService");
		ProcessInstance result = runtimeService.createProcessInstanceQuery().processInstanceId(delegateTask.getProcessInstanceId()).singleResult();
		String warehouseId = (String)runtimeService.getVariable(delegateTask.getExecutionId(), "warehouseId");
		Warehouse warehouse = warehouseService.getWarehouseById(warehouseId);
		List<SystemUser> users = warehouse.getManagers();
		List<String> userIds = new ArrayList<String>();
		for(SystemUser user:users){
			userIds.add(user.getId());
		}
	
		delegateTask.addCandidateUsers(userIds);
		
		String key = result.getBusinessKey();
		
		ProductSplit split = productSplitService.getProductSplitById(key);
		
		Map<String,Object> condition = new HashMap<String,Object>();
		condition = new HashMap<String,Object>();
		condition.put("id", split.getId());
		condition.put("status", ProductSplitStatus.CLRK);
		
		productSplitService.updateProductSplitStatus(condition);
	}
	
}
