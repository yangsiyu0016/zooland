package com.zoo.listener.activiti.productsplit;

import java.util.Date;
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
 * 流程完成
 * @author aa
 *
 */
@Component("productSplitCompleteHandler")
public class ProductSplitCompleteHandler implements ExecutionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4204304354707127035L;
	
	private ProcessEngine processEngine;
	//private StockService stockService;
	//private StockDetailService stockDetailService;
	//private JournalAccountService journalAccountService;
	private ProductSplitService productSplitService;

	@Override
	public void notify(DelegateExecution delegateExecution) {
		// TODO Auto-generated method stub
		processEngine = (ProcessEngine) ApplicationUtil.getBean("processEngine");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance result = runtimeService.createProcessInstanceQuery().processInstanceId(delegateExecution.getProcessInstanceId()).singleResult();
		String key = result.getBusinessKey();

		productSplitService = (ProductSplitService) ApplicationUtil.getBean("productSplitService");
		
		//获取拆分单
		ProductSplit split = productSplitService.getProductSplitById(key);
		
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("id", split.getId());
		condition.put("etime", new Date());
		condition.put("status", ProductSplitStatus.FINISHED);
		productSplitService.updateProductSplitStatus(condition);
	}

}
