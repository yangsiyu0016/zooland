package com.zoo.listener.activiti;



import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import com.zoo.controller.erp.constant.SellStatus;
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
	//private SellDetailService sellDetailService;
	@Override
	public void notify(DelegateTask delegateTask) {
		processEngine=(ProcessEngine) ApplicationUtil.getBean("processEngine");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		sellService = (SellService) ApplicationUtil.getBean("sellService");
		//sellDetailService = (SellDetailService) ApplicationUtil.getBean("sellDetailService");
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(delegateTask.getProcessInstanceId()).singleResult();
		String key = pi.getBusinessKey();
		Sell sell= sellService.getSellById(key);
		List<SellDetail> details =sell.getDetails(); //sellDetailService.getSellDetailBySellId(sell.getId());
		List<String> warehouseIdList = new ArrayList<String>();
		for(SellDetail detail:details){
			if(!warehouseIdList.contains(detail.getWarehouse().getId())) warehouseIdList.add(detail.getWarehouse().getId());
		}
		Map<String, Object> variables = delegateTask.getVariables();
		 
		variables.put("warehouseIds", warehouseIdList);
		delegateTask.setVariables(variables);
		sell.setStatus(SellStatus.OUT);
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("id", sell.getId());
		condition.put("status", SellStatus.OUT);
		condition.put("cwtgtime", new Date());
		sellService.updateSellStatus(condition);
	}
	
}
