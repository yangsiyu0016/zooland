package com.zoo.listener.activiti.assembled;

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
import com.zoo.service.erp.productsplit.ProductSplitService;
import com.zoo.utils.ApplicationUtil;

/**
 * 组装单重新提交
 * @author aa
 *
 */
@Component("AssembledResubmitHandler")
public class AssembledResubmitHandler implements ExecutionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ProcessEngine processEngine;
	private ProductAssembledService productAssembledService;
	
	@Override
	public void notify(DelegateExecution execution) {
		// TODO Auto-generated method stub
		processEngine = (ProcessEngine) ApplicationUtil.getBean("processEngine");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		productAssembledService = (ProductAssembledService)ApplicationUtil.getBean("productAssembledService");
		
		ProcessInstance result = runtimeService.createProcessInstanceQuery().processInstanceId(execution.getProcessInstanceId()).singleResult();
		String key = result.getBusinessKey();
		ProductAssembled assembled = productAssembledService.getProductAssembledById(key);
		Map<String,Object> condition = new HashMap<String,Object>();
		
		condition = new HashMap<String,Object>();
		condition.put("id", assembled.getId());
		condition.put("status", ProductAssembledStatus.ZGSH);
		productAssembledService.updateProductAssembledStatus(condition);
	}

}
