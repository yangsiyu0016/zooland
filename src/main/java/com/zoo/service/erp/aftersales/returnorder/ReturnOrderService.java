package com.zoo.service.erp.aftersales.returnorder;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.util.StringUtil;
import com.zoo.controller.erp.constant.ChangeOrderStatus;
import com.zoo.controller.erp.constant.ReturnOrderStatus;
import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.aftersales.returnorder.ReturnOrderDetailMapper;
import com.zoo.mapper.erp.aftersales.returnorder.ReturnOrderMapper;
import com.zoo.model.erp.aftersales.returnorder.ReturnOrder;
import com.zoo.model.erp.aftersales.returnorder.ReturnOrderDetail;
import com.zoo.model.system.user.UserInfo;
import com.zoo.utils.OrderCodeHelper;

@Service
public class ReturnOrderService {
	
	@Autowired
	ProcessEngine processEngine;
	
	@Autowired
	IdentityService identityService;
	
	@Autowired
	ReturnOrderMapper returnOrderMapper;
	
	@Autowired
	ReturnOrderDetailMapper detailMapper;
	
	/**
	 * 添加
	 */
	public void addReturnOrder(ReturnOrder order) {
		String id = UUID.randomUUID().toString();
		
		order.setId(id);
		if("AUTO".equals(order.getCodeGeneratorType())) {
			try {
				order.setCode(OrderCodeHelper.GenerateId("TH"));
			} catch (Exception e) {
				// TODO: handle exception
				throw new ZooException(ExceptionEnum.GENER_CODE_ERROR);
			}
		}
		order.setStatus(ReturnOrderStatus.WTJ);
		order.setCuserId(LoginInterceptor.getLoginUser().getId());
		order.setCtime(new Date());
		order.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		order.setIsClaimed("N");
		returnOrderMapper.addReturnOrder(order);
		for(ReturnOrderDetail detail: order.getDetails()) {
			 detail.setId(UUID.randomUUID().toString());
			 detail.setReturnOrderId(id);
			 detail.setCtime(new Date());
			 detailMapper.addDetail(detail);
		}
	}
	
	/**
	 * 分页查询
	 */
	public List<ReturnOrder> getReturnOrderByPage(Integer page, Integer size) {
		Integer start = null;
		if(page != null) {
			start = (page - 1) * size;
		}
		return returnOrderMapper.getReturnOrderByPage(start, size, LoginInterceptor.getLoginUser().getCompanyId());
	}
	
	/**
	 * 获取总条数
	 */
	public Long getCount() {
		return returnOrderMapper.getCount(LoginInterceptor.getLoginUser().getCompanyId());
	}
	
	/**
	 * 更新退货单流程状态
	 */
	public void updateReturnOrderStatus(Map<String, Object> condition) {
		returnOrderMapper.updateReturnOrderStatus(condition);
	}
	
	/**
	 * 根据id获取退货单
	 */
	public ReturnOrder getReturnOrderById(String id) {
		ReturnOrder order = returnOrderMapper.getReturnOrderById(id);
		return order;
	}
	
	/**
	 * 开启流程
	 */
	public void	startProcess(String id) {
		ReturnOrder order = this.getReturnOrderById(id);
		
		if(StringUtil.isNotEmpty(order.getProcessInstanceId())) {
			throw new ZooException(ExceptionEnum.FLOWSTATED);
		}
		
		UserInfo info = LoginInterceptor.getLoginUser();
		
		Map<String, Object> variables = new HashMap<String, Object>();
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(info.getId());
		
		String businessKey = id;
		variables.put("CODE", order.getCode());
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId("returnOrder", businessKey, variables, info.getCompanyId());
		
		String processInstanceId = processInstance.getId();
		
		returnOrderMapper.updateProcessInstanceId(processInstanceId, id);
		Map<String,Object> condition = new HashMap<String, Object>();
		
		condition.put("id", id);
		condition.put("status", ChangeOrderStatus.KG);
		
		this.updateReturnOrderStatus(condition);
	}
	
	/**
	 * 更新退货单
	 */
	public int updateReturnOrder(ReturnOrder order) {
		return returnOrderMapper.updateReturnOrder(order);
	}
	
	/**
	 * 作废
	 */
	public void destroy(String id) {
		ReturnOrder order = returnOrderMapper.getReturnOrderById(id);
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("id", id);
		condition.put("status", ReturnOrderStatus.DESTROY);
		condition.put("etime", new Date());
		returnOrderMapper.updateReturnOrderStatus(condition);
		
		//删除流程
		processEngine.getRuntimeService().deleteProcessInstance(order.getProcessInstanceId(), "待定");
		
		returnOrderMapper.updateProcessInstanceId(null, id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("isClaimed", "N");
		returnOrderMapper.updateReturnOrderIsClaimed(map);
	}
	
	/**
	 * 更新是否签收
	 */
	public int updateReturnOrderIsClaimed(Map<String, Object> variables) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("id", variables.get("id"));
		condition.put("isClaimed", "Y");
		
		return returnOrderMapper.updateReturnOrderIsClaimed(condition);
	}
	
	/**
	 * 流程取回
	 */
	public void reset(String id) {
		ReturnOrder order = this.getReturnOrderById(id);
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("id", id);
		condition.put("status", ReturnOrderStatus.WTJ);
		condition.put("isClaimed", "N");
		returnOrderMapper.updateReturnOrderStatus(condition);
		//删除流程
		processEngine.getRuntimeService().deleteProcessInstance(order.getProcessInstanceId(), "待定");
		
		returnOrderMapper.updateProcessInstanceId(null, id);
	}
	
}
