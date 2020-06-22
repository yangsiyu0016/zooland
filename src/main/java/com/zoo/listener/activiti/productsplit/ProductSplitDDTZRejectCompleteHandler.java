package com.zoo.listener.activiti.productsplit;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import com.zoo.controller.erp.constant.ProductSplitStatus;
import com.zoo.model.erp.productsplit.ProductSplit;
import com.zoo.service.erp.productsplit.ProductSplitService;
import com.zoo.utils.ApplicationUtil;

/**
 * 订单调整完成
 * @author aa
 *
 */
@Component("productSplitDDTZRejectCompleteHandler")
public class ProductSplitDDTZRejectCompleteHandler implements TaskListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6838819904012898429L;

	private ProcessEngine processEngine;
	
	private ProductSplitService productSplitService;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		processEngine = (ProcessEngine) ApplicationUtil.getBean("processEngine");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		
		productSplitService = (ProductSplitService) ApplicationUtil.getBean("productSplitService");
		
		ProcessInstance result = runtimeService.createProcessInstanceQuery().processInstanceId(delegateTask.getProcessInstanceId()).singleResult();
		
		String key = result.getBusinessKey();
		
		ProductSplit split = productSplitService.getProductSplitById(key);
		
		Map<String,Object> condition = new HashMap<String,Object>();
		condition = new HashMap<String,Object>();
		condition.put("id", split.getId());
		condition.put("status", ProductSplitStatus.CKZG);
		
		productSplitService.updateProductSplitStatus(condition);
	}

}
