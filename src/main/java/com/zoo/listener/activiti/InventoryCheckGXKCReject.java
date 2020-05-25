package com.zoo.listener.activiti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import com.zoo.controller.erp.constant.InventoryCheckStatus;
import com.zoo.model.erp.inventorycheck.InventoryCheck;
import com.zoo.model.system.position.Position;
import com.zoo.model.system.user.SystemUser;
import com.zoo.service.erp.inventorycheck.InventoryCheckService;
import com.zoo.service.system.position.PositionService;
import com.zoo.service.system.user.SystemUserService;
import com.zoo.utils.ApplicationUtil;

@Component("InventoryCheckGXKCReject")
public class InventoryCheckGXKCReject implements ExecutionListener{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private ProcessEngine processEngine;
	
	private SystemUserService systemUserService;

	private PositionService positionService;
	
	private InventoryCheckService inventoryCheckService;

	@Override
	public void notify(DelegateExecution execution) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				processEngine = (ProcessEngine) ApplicationUtil.getBean("processEngine");
				RuntimeService runtimeService = processEngine.getRuntimeService();
				inventoryCheckService = (InventoryCheckService)ApplicationUtil.getBean("inventoryCheckService");
				
				ProcessInstance result = runtimeService.createProcessInstanceQuery().processInstanceId(execution.getProcessInstanceId()).singleResult();
				String key = result.getBusinessKey();
				InventoryCheck ic = inventoryCheckService.getInventoryCheckById(key);
				
				Map<String,Object> condition = new HashMap<String,Object>();
				
				
				condition = new HashMap<String,Object>();
				condition.put("id", ic.getId());
				condition.put("status", InventoryCheckStatus.ZGSH);
				
				inventoryCheckService.updateInventoryCheckStatus(condition);
				
		/*
		 * List<Position> list = positionService.getPositionByCondition(condition);
		 * List<String> groupIds = new ArrayList<String>();
		 * 
		 * for(Position position : list) { groupIds.add(position.getId()); }
		 * delegateTask.addCandidateGroups(groupIds);
		 */
	}
	
	

	
	
}
