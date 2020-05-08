package com.zoo.service.flow;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.flow.CustomTaskMapper;
import com.zoo.model.flow.OpeningInventoryTask;
import com.zoo.model.flow.PurchaseTask;
import com.zoo.model.flow.SellTask;

@Service
@Transactional
public class CustomTaskService {
	@Autowired
	CustomTaskMapper taskMapper;
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
	public OpeningInventoryTask getOpeningInventoryTaskById(String taskId) {
		// TODO Auto-generated method stub
		return taskMapper.getOpeningInventoryTaskById(taskId);
	}
	public PurchaseTask getPurchaseTaskById(String taskId) {
		// TODO Auto-generated method stub
		return taskMapper.getPurchaseTaskById(taskId);
	}
}
