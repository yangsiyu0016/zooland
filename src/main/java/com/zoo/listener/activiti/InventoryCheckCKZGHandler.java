package com.zoo.listener.activiti;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

import com.zoo.model.system.position.Position;
import com.zoo.model.system.user.SystemUser;
import com.zoo.service.system.position.PositionService;
import com.zoo.service.system.user.SystemUserService;
import com.zoo.utils.ApplicationUtil;

@Component("inventoryCheckCKZGHandler")
public class InventoryCheckCKZGHandler implements TaskListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ProcessEngine processEngine;
	
	private SystemUserService systemUserService;

	private PositionService positionService;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		//获取流程引擎
		processEngine = (ProcessEngine)ApplicationUtil.getBean("processEngine");
		
		//获取runtimeService
		RuntimeService runtimeService = processEngine.getRuntimeService();
		systemUserService = (SystemUserService)ApplicationUtil.getBean("systemUserService");
		positionService = (PositionService)ApplicationUtil.getBean("positionService");
		
		String applyUserId = (String)runtimeService.getVariable(delegateTask.getExecutionId(), "applyUserId");
		
		SystemUser systemUser = systemUserService.getUserById(applyUserId);
		
		Map<String,Object> condition = new HashMap<String,Object>();
		
		condition.put("companyId", systemUser.getCompanyId());
		condition.put("code", "CKZG");
		
		List<Position> list = positionService.getPositionByCondition(condition);
		List<String> groupIds = new ArrayList<String>();
		
		for(Position position : list) {
			groupIds.add(position.getId());
		}
		delegateTask.addCandidateGroups(groupIds);
	}

}
