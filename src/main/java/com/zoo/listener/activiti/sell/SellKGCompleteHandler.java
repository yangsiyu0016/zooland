package com.zoo.listener.activiti.sell;



import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import com.zoo.controller.erp.constant.SellStatus;
import com.zoo.service.erp.sell.SellService;
import com.zoo.utils.ApplicationUtil;
/**
 * 出库完成
 * @author 52547
 *
 */
@Component("sellKGCompleteHandler")
public class SellKGCompleteHandler implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ProcessEngine processEngine;
	private SellService sellService;
	//private SellDetailService sellDetailService;
	@Override
	public void notify(DelegateTask delegateTask) {
		processEngine=(ProcessEngine) ApplicationUtil.getBean("processEngine");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		sellService = (SellService) ApplicationUtil.getBean("sellService");
		//sellDetailService = (SellDetailService) ApplicationUtil.getBean("sellDetailService");
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(delegateTask.getProcessInstanceId()).singleResult();
		String key = pi.getBusinessKey();
	
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("id", key);
		condition.put("status", SellStatus.FINISHED);
		//condition.put("cwtgtime", new Date());
		sellService.updateSellStatus(condition);
	}
	
}
