package com.zoo.listener.activiti.productsplit;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import com.zoo.controller.erp.constant.ProductSplitStatus;
import com.zoo.model.erp.productsplit.ProductSplit;
import com.zoo.service.erp.productsplit.ProductSplitService;
import com.zoo.utils.ApplicationUtil;

/**
 * 拆分单作废
 * @author aa
 *
 */
@Component("ProductSplitDestroyHandler")
public class ProductSplitDestroyHandler implements ExecutionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5773711812334433313L;

	private ProcessEngine processEngine;
	
	private ProductSplitService productSplitService;
	
	@Override
	public void notify(DelegateExecution execution) {
		// TODO Auto-generated method stub
		processEngine = (ProcessEngine) ApplicationUtil.getBean("processEngine");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		productSplitService = (ProductSplitService)ApplicationUtil.getBean("productSplitService");
		
		ProcessInstance result = runtimeService.createProcessInstanceQuery().processInstanceId(execution.getProcessInstanceId()).singleResult();
		String key = result.getBusinessKey();
		ProductSplit split = productSplitService.getProductSplitById(key);
		
		Map<String,Object> condition = new HashMap<String,Object>();
		
		condition = new HashMap<String,Object>();
		condition.put("id", split.getId());
		condition.put("status", ProductSplitStatus.DESTROY);
		
		productSplitService.updateProductSplitStatus(condition);
	}

}
