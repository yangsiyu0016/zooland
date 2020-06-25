package com.zoo.service.flow;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.flow.CustomTaskMapper;
import com.zoo.model.flow.AssembledTask;
import com.zoo.model.flow.InventoryCheckTask;
import com.zoo.model.flow.OpeningInventoryTask;
import com.zoo.model.flow.PurchaseTask;
import com.zoo.model.flow.SellTask;
import com.zoo.service.erp.inventorycheck.InventoryCheckService;
import com.zoo.service.erp.openingInventory.OpeningInventoryService;
import com.zoo.service.erp.purchase.PurchaseService;
import com.zoo.service.erp.sell.SellService;

@Service
@Transactional
public class CustomTaskService {
	@Autowired
	CustomTaskMapper taskMapper;
	
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	InventoryCheckService inventoryCheckService;
	
	@Autowired
	private SellService sellService;
	
	@Autowired
	private PurchaseService purchaseService;
	
	@Autowired
	private OpeningInventoryService openingInventoryService;
	public List<AssembledTask> getAssembledTask(Integer page, Integer size) {
		Integer start = (page-1)*size;
		return taskMapper.getAssembledTask(start,size,LoginInterceptor.getLoginUser().getId());
	}
	public long getAssembledTaskCount() {
		// TODO Auto-generated method stub
		return taskMapper.getAssembledTaskCount(LoginInterceptor.getLoginUser().getId());
	}
	public List<OpeningInventoryTask> getOpeningInventoryTask(Integer page,Integer size){
		Integer start = (page-1)*size;
		return taskMapper.getOpeningInventoryTask(start, size, LoginInterceptor.getLoginUser().getId());
	}
	public long getOpeningInventoryTaskCount() {
		return taskMapper.getOpeningInventoryTaskCount(LoginInterceptor.getLoginUser().getId());
	}
	public List<SellTask> getSellTask(Integer page, Integer size) {
		Integer start = (page-1)*size;
		return taskMapper.getSellTask(start,size,LoginInterceptor.getLoginUser().getId());
	}
	public long getSellTaskCount() {
		// TODO Auto-generated method stub
		return taskMapper.getSellTaskCount(LoginInterceptor.getLoginUser().getId());
	}
	public List<PurchaseTask> getPruchaseTask(Integer page, Integer size) {
		Integer start = (page-1)*size;
		return taskMapper.getPurchaseTask(start,size,LoginInterceptor.getLoginUser().getId());
	}
	public long getPurchaseTaskCount() {
		// TODO Auto-generated method stub
		return taskMapper.getPurchaseTaskCount(LoginInterceptor.getLoginUser().getId());
	}
	public List<InventoryCheckTask> getInventoryCheckTask(Integer page, Integer size) {
		Integer start = (page-1)*size;
		return taskMapper.getInventoryCheckTask(start,size,LoginInterceptor.getLoginUser().getId());
	}
	public long getInventoryCheckTaskCount() {
		// TODO Auto-generated method stub
		return taskMapper.getInventoryCheckTaskCount(LoginInterceptor.getLoginUser().getId());
	}
	public OpeningInventoryTask getOpeningInventoryTaskById(String taskId) {
		// TODO Auto-generated method stub
		return taskMapper.getOpeningInventoryTaskById(taskId);
	}
	public AssembledTask getAssembledTaskById(String taskId) {
		return taskMapper.getAssembledTaskById(taskId);
	}
	public PurchaseTask getPurchaseTaskById(String taskId) {
		// TODO Auto-generated method stub
		return taskMapper.getPurchaseTaskById(taskId);
	}
	public SellTask getSellTaskById(String taskId) {
		// TODO Auto-generated method stub
		return taskMapper.getSellTaskById(taskId);
	}
	public InventoryCheckTask getInventoryCheckTaskById(String taskId) {
		// TODO Auto-generated method stub
		return taskMapper.getInventoryCheckTaskById(taskId);
	}
	
	public void claim(String taskId, String id) {
		taskService.claim(taskId, LoginInterceptor.getLoginUser().getId());
		Map<String, Object> variables = taskService.getVariables(taskId);
		variables.put("id", id);
		String string = variables.get("CODE").toString().substring(0, 2);
		if("PD".equals(string)) {
			inventoryCheckService.updateInventoryCheckIsClaimed(variables);
		}else if ("XS".equals(string)) {
			sellService.updateSellIsClaimed(variables);
		}else if ("CG".equals(string)) {
			purchaseService.updatePurchaseIsClaimed(variables);
		}else if ("QC".equals(string)) {
			openingInventoryService.updateOpeningInventoryIsClaimed(variables);
		}
	}
}
