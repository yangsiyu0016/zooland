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
 * 订单调整完成
 * @author aa
 *
 */@Component("SellDDTZRejectCompleteHandler")
public class SellDDTZRejectCompleteHandler implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2754046546477315871L;

	private ProcessEngine processEngine;
	
	private SellService sellService;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		processEngine = (ProcessEngine) ApplicationUtil.getBean("processEngine");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		sellService = (SellService) ApplicationUtil.getBean("sellService");
		
		ProcessInstance result = runtimeService.createProcessInstanceQuery().processInstanceId(delegateTask.getProcessInstanceId()).singleResult();
		String key = result.getBusinessKey();
		
		Sell sell = sellService.getSellById(key);
		
		Map<String,Object> condition = new HashMap<String,Object>();
		
		//更新sell表中下一节点办理人
		condition = new HashMap<String,Object>();
		condition.put("id", sell.getId());
		condition.put("status", SellStatus.CWSH);
		
		sellService.updateSellStatus(condition);
	}

}
