package com.zoo.service.erp.aftersales.repairorder;

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
import com.zoo.controller.erp.constant.RepairOrderStatus;
import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.aftersales.repairorder.RepairOrderDetailMapper;
import com.zoo.mapper.erp.aftersales.repairorder.RepairOrderMapper;
import com.zoo.mapper.erp.product.SpecParamMapper;
import com.zoo.model.erp.aftersales.repairorder.RepairOrder;
import com.zoo.model.erp.aftersales.repairorder.RepairOrderDetail;
import com.zoo.model.system.user.UserInfo;
import com.zoo.utils.OrderCodeHelper;

@Service
public class RepairOrderService {

	@Autowired
	RepairOrderMapper repairOrderMapper;
	
	@Autowired
	RepairOrderDetailMapper detailMapper;
	
	@Autowired
	SpecParamMapper paramMapper;
	
	@Autowired
	ProcessEngine processEngine;
	
	@Autowired
	IdentityService identityService;
	
	/**
	 * 添加
	 */
	public void addRepairOrder(RepairOrder order) {
		String id = UUID.randomUUID().toString();
		
		order.setId(id);
		if("AUTO".equals(order.getCodeGeneratorType())) {
			try {
				order.setCode(OrderCodeHelper.GenerateId("WX"));
			} catch (Exception e) {
				// TODO: handle exception
				throw new ZooException(ExceptionEnum.GENER_CODE_ERROR);
			}
		}
		order.setStatus(RepairOrderStatus.WTJ);
		order.setCuserId(LoginInterceptor.getLoginUser().getId());
		order.setCtime(new Date());
		order.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		order.setIsClaimed("N");
		repairOrderMapper.addRepairOrder(order);
		
		for(RepairOrderDetail detail: order.getDetails()) {
			detail.setId(UUID.randomUUID().toString());
			detail.setRepairOrderId(id);
			detail.setCtime(new Date());
			detailMapper.addDetail(detail);
		}
	}
	
	/**
	 * 分页查询
	 */
	public List<RepairOrder> getRepairOrderByPage(Integer page, Integer size) {
		Integer start = null;
		if(page !=null) {
			start = (page - 1) * size;
		}
		
		List<RepairOrder> list = repairOrderMapper.getRepairOrderByPage(start, size, LoginInterceptor.getLoginUser().getCompanyId());
		
		return list;
	}
	
	/**
	 * 获取总条数
	 */
	public Long getCount() {
		return repairOrderMapper.getCount(LoginInterceptor.getLoginUser().getCompanyId());
	}
	
	/**
	 * 更新维修单流程状态
	 */
	public void updateRepairOrderStatus(Map<String, Object> condition) {
		repairOrderMapper.updateRepairOrderStatus(condition);
	}
	
	/**
	 * 根据id获取维修单
	 */
	public RepairOrder getRepairOrderById(String id) {
		
		RepairOrder repairOrder = repairOrderMapper.getRepairOrderById(id);
		
		return repairOrder;
	}
	
	/**
	 * 开启流程
	 */
	public void	startProcess(String id) {
		RepairOrder repairOrder = this.getRepairOrderById(id);
		if(StringUtil.isNotEmpty(repairOrder.getProcessInstanceId())) {
			throw new ZooException(ExceptionEnum.FLOWSTATED);
		}
		
		UserInfo info = LoginInterceptor.getLoginUser();
		
		Map<String, Object> variables = new HashMap<String, Object>();
		
		identityService.setAuthenticatedUserId(info.getId());
		
		String businessKey = id;
		
		variables.put("CODE", repairOrder.getCode());
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId("repairOrder", businessKey, variables, info.getCompanyId());
		
		String processInstanceId = processInstance.getId();
		repairOrderMapper.updateProcessInstanceId(processInstanceId, id);
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("id", id);
		condition.put("status", RepairOrderStatus.WX);
		this.updateRepairOrderStatus(condition);
	}
	
	/**
	 * 更新维修单
	 */
	public int updateRepairOrder(RepairOrder repairOrder) {
		return repairOrderMapper.updateRepairOrder(repairOrder);
	}
	
	/**
	 * 作废
	 */
	public void	destroy(String id) {
		RepairOrder order = this.getRepairOrderById(id);
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("id", id);
		condition.put("status", RepairOrderStatus.DESTROY);
		condition.put("etime", new Date());
		
		repairOrderMapper.updateRepairOrderStatus(condition);
		//删除流程
		processEngine.getRuntimeService().deleteProcessInstance(order.getProcessInstanceId(), "待定");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("isClaimed", "N");
		repairOrderMapper.updateRepairOrderIsClaimed(map);
	}
	
	/**
	 * 更新是否签收
	 */
	public int updateRepairOrderIsClaimed(Map<String, Object> variables) {
		Map< String, Object> condition = new HashMap<String, Object>();
		
		condition.put("id", variables.get("id"));
		condition.put("isClaimed", "Y");
		
		return repairOrderMapper.updateRepairOrderIsClaimed(condition);
	}
	
	/**
	 * 流程取回
	 */
	public void	reset(String id) {
		RepairOrder order = this.getRepairOrderById(id);
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("id", id);
		condition.put("status", RepairOrderStatus.WTJ);
		condition.put("isClaimed", "N");
		repairOrderMapper.updateRepairOrderStatus(condition);
		
		//删除流程
		processEngine.getRuntimeService().deleteProcessInstance(order.getProcessInstanceId(), "待定");
		
		repairOrderMapper.updateProcessInstanceId(null, id);
	}
	
}
