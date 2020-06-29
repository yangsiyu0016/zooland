package com.zoo.listener.activiti.sell;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.sell.Sell;
import com.zoo.model.erp.sell.SellDetail;
import com.zoo.service.erp.sell.SellService;
import com.zoo.utils.ApplicationUtil;
/**
 * 财务审核完成
 * @author 52547
 *
 */
@Component("sellCWCompleteHandler")
public class SellCWCompleteHandler implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ProcessEngine processEngine;
	private SellService sellService;
	@Override
	public void notify(DelegateTask delegateTask) {
		processEngine=(ProcessEngine) ApplicationUtil.getBean("processEngine");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		sellService = (SellService) ApplicationUtil.getBean("sellService");
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(delegateTask.getProcessInstanceId()).singleResult();
		String key = pi.getBusinessKey();
		Sell sell= sellService.getSellById(key);
		List<SellDetail> details = sell.getDetails();
		List<String> warehouseIdList = new ArrayList<String>();
		for(SellDetail detail:details){
			if(!warehouseIdList.contains(detail.getWarehouse().getId())) warehouseIdList.add(detail.getWarehouse().getId());
		}
		Map<String, Object> variables = delegateTask.getVariables();
		 
		variables.put("warehouseIds", warehouseIdList);
		delegateTask.setVariables(variables);
	}
	
}
