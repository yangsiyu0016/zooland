package com.zoo.service.flow;

import java.util.List;

import javax.transaction.Transactional;

import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.flow.CustomTaskMapper;
import com.zoo.model.flow.AssembledTask;
import com.zoo.model.flow.InventoryCheckTask;
import com.zoo.model.flow.OpeningInventoryTask;
import com.zoo.model.flow.ProductSplitTask;
import com.zoo.model.flow.PurchaseTask;
import com.zoo.model.flow.SellTask;
import com.zoo.service.erp.inventorycheck.InventoryCheckService;

@Service
@Transactional
public class CustomTaskService {
	@Autowired
	CustomTaskMapper taskMapper;
	
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	InventoryCheckService inventoryCheckService;
	public List<AssembledTask> getAssembledTask(Integer page,Integer size,String sort,String order,String keywords) {
		Integer start = (page-1)*size;
		return taskMapper.getAssembledTask(start,size,sort,order,keywords,LoginInterceptor.getLoginUser().getId());
	}
	public long getAssembledTaskCount(String keywords) {
		// TODO Auto-generated method stub
		return taskMapper.getAssembledTaskCount(keywords,LoginInterceptor.getLoginUser().getId());
	}
	public List<OpeningInventoryTask> getOpeningInventoryTask(Integer page,Integer size,String sort,String order,String keywords){
		Integer start = (page-1)*size;
		return taskMapper.getOpeningInventoryTask(start, size, sort,order,keywords,LoginInterceptor.getLoginUser().getId());
	}
	public long getOpeningInventoryTaskCount(String keywords) {
		return taskMapper.getOpeningInventoryTaskCount(keywords,LoginInterceptor.getLoginUser().getId());
	}
	public List<SellTask> getSellTask(Integer page, Integer size,String sort,String order,String keywords) {
		Integer start = (page-1)*size;
		return taskMapper.getSellTask(start,size,sort,order,keywords,LoginInterceptor.getLoginUser().getId());
	}
	public long getSellTaskCount(String keywords) {
		// TODO Auto-generated method stub
		return taskMapper.getSellTaskCount(keywords,LoginInterceptor.getLoginUser().getId());
	}
	public List<PurchaseTask> getPruchaseTask(Integer page, Integer size,String sort,String order,String keywords) {
		Integer start = (page-1)*size;
		return taskMapper.getPurchaseTask(start,size,sort,order,keywords,LoginInterceptor.getLoginUser().getId());
	}
	public long getPurchaseTaskCount(String keywords) {
		// TODO Auto-generated method stub
		return taskMapper.getPurchaseTaskCount(keywords,LoginInterceptor.getLoginUser().getId());
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
	/*--------------拆分单流程任务代码开始-----------------*/
	//分页查询
	public List<ProductSplitTask> getProductSplitTask(Integer page,Integer size,String sort,String order,String keywords) {
		// TODO Auto-generated method stub
		Integer start = (page-1)*size;
		return taskMapper.getProductSplitTask(start,size,sort,order,keywords,LoginInterceptor.getLoginUser().getId());
	}
	//总数量
	public Long getProductSplitTaskCount(String keywords) {
		// TODO Auto-generated method stub
		return taskMapper.getProductSplitTaskCount(keywords,LoginInterceptor.getLoginUser().getId());
	}
	//根据任务id获取数据
	public ProductSplitTask getProductSplitTaskById(String taskId) {
		// TODO Auto-generated method stub
		return taskMapper.getProductSplitTaskById(taskId);
	}
	
}
