package com.zoo.listener.activiti.assembled;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import com.zoo.controller.erp.constant.ProductAssembledStatus;
import com.zoo.controller.erp.constant.ProductSplitStatus;
import com.zoo.model.erp.assembled.ProductAssembled;
import com.zoo.service.erp.assembled.ProductAssembledService;
import com.zoo.utils.ApplicationUtil;

/**
 * 库管完成
 * @author aa
 *
 */
@Component("AssembledKGCompleteHandler")
public class AssembledKGCompleteHandler implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8590011899330365382L;

	private ProcessEngine processEngine;
	
	private ProductAssembledService productAssembledService;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		processEngine = (ProcessEngine) ApplicationUtil.getBean("processEngine");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance result = runtimeService.createProcessInstanceQuery().processInstanceId(delegateTask.getProcessInstanceId()).singleResult();
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
