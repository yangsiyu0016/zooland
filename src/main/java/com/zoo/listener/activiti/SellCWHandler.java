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

/**
 * 财务
 * @author 52547
 *
 */
@Component("sellCWHandler")
public class SellCWHandler implements TaskListener {

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
		RuntimeService runtimeService = processEngine.getRuntimeService();
		systemUserService = (SystemUserService) ApplicationUtil.getBean("systemUserService");
		positionService = (PositionService) ApplicationUtil.getBean("positionService");
		String applyUserId =  (String)runtimeService.getVariable(delegateTask.getExecutionId(), "applyUserId");
		//String key = (String)runtimeService.getVariable(delegateTask.getExecutionId(), "key");
		//String from = (String)runtimeService.getVariable(delegateTask.getExecutionId(), "from");
		SystemUser applyUser = systemUserService.getUserById(applyUserId);
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("companyId", applyUser.getCompanyId());
		condition.put("code", "CW");
		List<Position> list = positionService.getPositionByCondition(condition);
		List<String> groupIds = new ArrayList<String>();
		for(Position position:list){
			groupIds.add(position.getId());
		}
		delegateTask.addCandidateGroups(groupIds);
		
		//Map<String,Object> variables = pi.getProcessVariables();
		//delegateTask.setVariables(variables);
	}
	
}
