package com.zoo.controller.flow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoo.filter.LoginInterceptor;
import com.zoo.model.flow.OpeningInventoryTask;
import com.zoo.model.flow.PurchaseTask;
import com.zoo.model.flow.SellTask;
import com.zoo.service.flow.CustomTaskService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/flow/task")
public class TaskController {
	@Autowired
	CustomTaskService customTaskService;
	@Autowired
	TaskService taskService;
	@GetMapping("getPurchaseTask")
	public Map<String,Object> getPurchaseTask(@RequestParam("page")Integer page,@RequestParam("size")Integer size){
		Map<String,Object> map = new HashMap<String,Object>();
		List<PurchaseTask> tasks =  customTaskService.getPruchaseTask(page,size);
		long count  = customTaskService.getPurchaseTaskCount();
		map.put("tasks", tasks);
		map.put("count", count);
		return map;
	}
	@GetMapping("getSellTask")
	public Map<String,Object> getSellTask(@RequestParam("page")Integer page,@RequestParam("size")Integer size){
		Map<String,Object> map = new HashMap<String,Object>();
		List<SellTask> tasks = customTaskService.getSellTask(page,size);
		long count = customTaskService.getSellTaskCount();
		map.put("tasks", tasks);
		map.put("count", count);
		return map;
	}
	@GetMapping("getOpeningInventoryTask")
	public Map<String,Object> getOpeningInventoryTask(@RequestParam("page")Integer page,@RequestParam("size")Integer size){
		Map<String,Object> map = new HashMap<String,Object>();
		List<OpeningInventoryTask> tasks = customTaskService.getOpeningInventoryTask(page, size);
		long count = customTaskService.getOpeningInventoryTaskCount();
		map.put("tasks", tasks);
		map.put("count", count);
		return map;
	}
	@GetMapping("getOpeningInventoryTaskById")
	public OpeningInventoryTask getOpeningInventoryTaskById(String taskId) {
		return customTaskService.getOpeningInventoryTaskById(taskId);
	}
	
	@PostMapping("claim")
	public RespBean claim(@RequestParam("taskId")String taskId) {
		try {
			taskService.claim(taskId, LoginInterceptor.getLoginUser().getId());
			return new RespBean("200","签收成功");
		} catch (Exception e) {
			return new RespBean("500","签收失败");
		}
	}
	@PostMapping("complete")
	public RespBean complete(@RequestParam("taskId")String taskId,@RequestParam("comment")String comment) {
		try {
			Map<String, Object> variables = new HashMap<String, Object>();
			if(StringUtils.isNotBlank(comment)){
				variables.put("comment", comment);
			}
			Task task=taskService.createTaskQuery().taskId(taskId).singleResult();
			String processInstanceId=task.getProcessInstanceId();
			Authentication.setAuthenticatedUserId(LoginInterceptor.getLoginUser().getId());
			taskService.addComment(taskId, processInstanceId, comment);
			taskService.complete(taskId,variables);
			return new RespBean("200","签收成功");
		} catch (Exception e) {
			return new RespBean("500","签收失败");
		}
	}
	public static void main(String[] args) {
		float i=1;
		float z= 100000;
		float total = z;
		float bei = (float) 1.2;
		System.out.println("第"+i+"期:总压注"+total+";当期压注："+z+";盈利："+(z*6-total));
		while(i<20) {
			z=z*bei;
			if(i==7) bei = (float) 1.2;
			i++;
			total= (float) (total+z);
			System.out.println("第"+i+"期:总压注"+total+";当期压注："+z+";盈利："+(z*6-total));
			
		}
	}
}
