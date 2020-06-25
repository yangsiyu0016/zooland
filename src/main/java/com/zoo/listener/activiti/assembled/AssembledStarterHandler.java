package com.zoo.listener.activiti.assembled;



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
@Component("assembledStarterHandler")
public class AssembledStarterHandler implements TaskListener{
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
			
	}

}
