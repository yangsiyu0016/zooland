package com.zoo.listener.activiti.assembled;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import com.zoo.controller.erp.constant.ProductAssembledStatus;
import com.zoo.model.erp.assembled.ProductAssembled;
import com.zoo.service.erp.assembled.ProductAssembledService;
import com.zoo.utils.ApplicationUtil;

/**
 * 流程完成
 * @author aa
 *
 */
@Component("AssembledCompleteHandler")
public class AssembledCompleteHandler implements ExecutionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7503758582015610753L;

	private ProcessEngine processEngine;
	
	private ProductAssembledService productAssembledService;
	
	@Override
	public void notify(DelegateExecution execution) {
		// TODO Auto-generated method stub
		processEngine = (ProcessEngine) ApplicationUtil.getBean("processEngine");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance result = runtimeService.createProcessInstanceQuery().processInstanceId(execution.getProcessInstanceId()).singleResult();
		String key = result.getBusinessKey();
		
		productAssembledService = (ProductAssembledService) ApplicationUtil.getBean("productAssembledService");
		
		ProductAssembled assembled = productAssembledService.getProductAssembledById(key);
		
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("id", assembled.getId());
		condition.put("etime", new Date());
		condition.put("status", ProductAssembledStatus.FINISHED);
		
		productAssembledService.updateProductAssembledStatus(condition);
	}

}
