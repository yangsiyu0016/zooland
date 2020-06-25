package com.zoo.listener.activiti.sell;



import java.util.ArrayList;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import org.springframework.stereotype.Component;

import com.zoo.model.erp.warehouse.Warehouse;
import com.zoo.model.system.user.SystemUser;
import com.zoo.service.erp.warehouse.WarehouseService;
import com.zoo.utils.ApplicationUtil;
/**
 * wo Warehouse Operator 库管
 * @author 52547
 *
 */
@Component("sellKGHandler")
public class SellKGHandler implements TaskListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ProcessEngine processEngine;
	private WarehouseService warehouseService;
	@Override
	public void notify(DelegateTask delegateTask) {
		processEngine = (ProcessEngine)ApplicationUtil.getBean("processEngine");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		warehouseService = (WarehouseService)ApplicationUtil.getBean("warehouseService");
		String warehouseId = (String)runtimeService.getVariable(delegateTask.getExecutionId(), "warehouseId");
		Warehouse warehouse = warehouseService.getWarehouseById(warehouseId);
		List<SystemUser> users = warehouse.getManagers();
		List<String> userIds = new ArrayList<String>();
		for(SystemUser user:users){
			userIds.add(user.getId());
		}
		delegateTask.addCandidateUsers(userIds);	
	}

}
