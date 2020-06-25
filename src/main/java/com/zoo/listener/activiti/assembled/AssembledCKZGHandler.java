package com.zoo.listener.activiti.assembled;

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

/**
 * 仓库主管
 * @author 86180
 *
 */
@Component("AssembledCKZGHandler")
public class AssembledCKZGHandler implements TaskListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ProcessEngine processEngine;
	
	private SystemUserService systemUserService;

	private PositionService positionService;
	@Override
	public void notify(DelegateTask delegateTask) {
		processEngine = (ProcessEngine) ApplicationUtil.getBean("processEngine");
		RuntimeService runtimeService = processEngine.getRuntimeService(); //(RuntimeService) ApplicationUtil.getBean("runtimeService");
		systemUserService = (SystemUserService) ApplicationUtil.getBean("systemUserService");
		positionService = (PositionService) ApplicationUtil.getBean("positionService");
		String applyUserId =  (String)runtimeService.getVariable(delegateTask.getExecutionId(), "applyUserId");
		SystemUser applyUser = systemUserService.getUserById(applyUserId);
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("companyId", applyUser.getCompanyId());
		condition.put("code", "CKZG");
		List<Position> list = positionService.getPositionByCondition(condition);
		List<String> groupIds = new ArrayList<String>();
		for(Position position:list){
			groupIds.add(position.getId());
		}
		delegateTask.addCandidateGroups(groupIds);
		
	}
	
}
