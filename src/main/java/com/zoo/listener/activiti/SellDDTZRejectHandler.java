package com.zoo.listener.activiti;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import com.zoo.controller.erp.constant.SellStatus;
import com.zoo.model.erp.sell.Sell;
import com.zoo.service.erp.sell.SellService;
import com.zoo.utils.ApplicationUtil;

/**
 * 订单调整
 * @author aa
 *
 */
@Component("SellDDTZRejectHandler")
public class SellDDTZRejectHandler implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ProcessEngine processEngine;
	
	private SellService sellService;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		processEngine = (ProcessEngine) ApplicationUtil.getBean("processEngine");
		sellService = (SellService) ApplicationUtil.getBean("sellService");
		
		RuntimeService runtimeService = processEngine.getRuntimeService();
		
		ProcessInstance result = runtimeService.createProcessInstanceQuery().processInstanceId(delegateTask.getProcessInstanceId()).singleResult();
		String key = result.getBusinessKey();
		
		Sell sell = sellService.getSellById(key);
		
		Map<String,Object> condition = new HashMap<String,Object>();
		//添加当前办理人id
		delegateTask.addCandidateUser(sell.getCuserId());
		//更新状态
		condition = new HashMap<String,Object>();
		condition.put("id", sell.getId());
		condition.put("status", SellStatus.REJECT);
		
		sellService.updateSellStatus(condition);
	}

}
