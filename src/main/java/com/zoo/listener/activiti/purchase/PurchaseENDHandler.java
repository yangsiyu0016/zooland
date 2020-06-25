package com.zoo.listener.activiti.purchase;



import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import com.zoo.controller.erp.constant.PurchaseStatus;

import com.zoo.service.erp.purchase.PurchaseService;
import com.zoo.utils.ApplicationUtil;
/**
 * 流程完成
 * @author 52547
 *
 */
@Component("purchaseENDHandler")
public class PurchaseENDHandler implements ExecutionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private ProcessEngine processEngine;
	private PurchaseService purchaseService;
	
	@Override
	public void notify(DelegateExecution execution) {
		processEngine =  (ProcessEngine) ApplicationUtil.getBean("processEngine");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		purchaseService = (PurchaseService) ApplicationUtil.getBean("purchaseService");
		
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(execution.getProcessInstanceId()).singleResult();
		String key = pi.getBusinessKey();
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("id", key);
		condition.put("status", PurchaseStatus.FINISHED);
		condition.put("etime", new Date());
		purchaseService.updatePurchaseStatus(condition);
		
	}
	public static void main(String[] args) {
		while(true) {
			try {
				Thread.sleep(1000);
				int a = (int) (Math.random()*6)+1;
				int b = (int) (Math.random()*6)+1;
				int c = (int) (Math.random()*6)+1;
				System.out.println(a+"+"+b+"+"+c+"="+(a+b+c));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
