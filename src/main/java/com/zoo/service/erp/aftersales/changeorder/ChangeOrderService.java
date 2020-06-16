package com.zoo.service.erp.aftersales.changeorder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.util.StringUtil;
import com.zoo.controller.erp.constant.ChangeOrderStatus;
import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.aftersales.changeorder.ChangeOrderDetailMapper;
import com.zoo.mapper.erp.aftersales.changeorder.ChangeOrderMapper;
import com.zoo.mapper.erp.product.SpecParamMapper;
import com.zoo.model.erp.aftersales.changeorder.ChangeOrder;
import com.zoo.model.erp.aftersales.changeorder.ChangeOrderDetail;
import com.zoo.model.erp.product.ProductSku;
import com.zoo.model.erp.product.SpecParam;
import com.zoo.model.system.user.UserInfo;
import com.zoo.utils.OrderCodeHelper;

import net.sf.json.JSONObject;

@Service
public class ChangeOrderService {

	@Autowired
	ChangeOrderMapper changeOrderMapper;
	
	@Autowired
	SpecParamMapper paramMapper;
	
	@Autowired
	ChangeOrderDetailMapper detailMapper;
	
	@Autowired
	ProcessEngine processEngine;
	
	@Autowired
	IdentityService identityService;
	
	/**
	 * 添加
	 */
	public void addChangeOrder(ChangeOrder changeOrder) {
		String id = UUID.randomUUID().toString();
		
		changeOrder.setId(id);
		if("AUTO".equals(changeOrder.getCodeGeneratorType())) {
			try {
				changeOrder.setCode(OrderCodeHelper.GenerateId("HH"));
			} catch (Exception e) {
				// TODO: handle exception
				throw new ZooException(ExceptionEnum.GENER_CODE_ERROR);
			}
		}
		changeOrder.setStatus(ChangeOrderStatus.WTJ);
		changeOrder.setCuserId(LoginInterceptor.getLoginUser().getId());
		changeOrder.setCtime(new Date());
		changeOrder.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		changeOrder.setIsClaimed("N");
		changeOrderMapper.addChangeOrder(changeOrder);
		
		for(ChangeOrderDetail detail: changeOrder.getDetails()) {
			detail.setId(UUID.randomUUID().toString());
			detail.setChangeOrderId(id);
			detail.setCtime(new Date());
			detailMapper.addDetail(detail);
		}
	}
	
	/**
	 * 分页查询
	 */
	public List<ChangeOrder> getChangeOrderByPage(Integer page, Integer size) {
		Integer start = null;
		if(page != null) {
			start = (page -1) * size;
		}
		
		List<ChangeOrder> list = changeOrderMapper.getChangeOrderByPage(start, size, LoginInterceptor.getLoginUser().getCompanyId());
		
		return list;
	}
	
	/**
	 * 获取总条数
	 * @return
	 */
	public Long getCount() {
		return changeOrderMapper.getCount(LoginInterceptor.getLoginUser().getCompanyId());
	}
	
	/**
	 * 更新换货单流程状态
	 * @param condition
	 */
	public void updateChangeOrderStatus(Map<String, Object> condition) {
		changeOrderMapper.updateChangeOrderStatus(condition);
	}
	
	/**
	 * 根据id获取换货单
	 * @param id
	 * @return
	 */
	public ChangeOrder getChangeOrderById(String id) {
		ChangeOrder order = changeOrderMapper.getChangeOrderById(id);
		List<String> built = new ArrayList<String>();
		for(ChangeOrderDetail detail: order.getDetails()) {
			ProductSku sku = detail.getProductSku();
			if(!built.contains(sku.getProduct().getId())) {
				String genericSpec = sku.getProduct().getProductDetail().getGenericSpec();
				Map<String,String> map = new HashMap<String, String>();
				JSONObject obj = JSONObject.fromObject(genericSpec);
				Set<String> keySet = obj.keySet();
				for(String key: keySet) {
					SpecParam param = paramMapper.getParamById(key);
					map.put(param.getName(), StringUtils.isBlank(obj.getString(key))?"其它":obj.getString(key));
				}
				sku.getProduct().getProductDetail().setGenericSpec(map.toString());
				String ownSpec = sku.getOwnSpec();
				map = new HashMap<String, String>();
				
				obj = JSONObject.fromObject(ownSpec);
				keySet = obj.keySet();
				for(String key: keySet) {
					SpecParam param = paramMapper.getParamById(key);
					map.put(param.getName(), obj.getString(key));
				}
				sku.setOwnSpec(ownSpec);
				
				detail.setProductSku(sku);
				built.add(sku.getProduct().getId());
			}
		}
		return order;
	}
	
	/**
	 * 开启流程
	 */
	public void startProcess(String id) {
		ChangeOrder order = this.getChangeOrderById(id);
		if(StringUtil.isNotEmpty(order.getProcessInstanceId())) {
			throw new ZooException(ExceptionEnum.FLOWSTATED);
		}
		
		UserInfo user = LoginInterceptor.getLoginUser();
		
		Map<String,Object> variables = new HashMap<String, Object>();
		
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(user.getId());
		
		String businessKey = id;
		variables.put("CODE", order.getCode());
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId("changeOrder", businessKey, variables, user.getCompanyId());
		String processInstanceId = processInstance.getId();
		
		changeOrderMapper.updateProcessInstanceId(processInstanceId, id);
		Map<String,Object> condition = new HashMap<String, Object>();
		
		condition.put("id", id);
		condition.put("status", ChangeOrderStatus.KG);
		
		this.updateChangeOrderStatus(condition);
	}
	/**
	 * 更新换货单
	 */
	public int updateChangeOrder(ChangeOrder order) {
		return changeOrderMapper.updateChangeOrder(order);
	}
	
	/**
	 * 作废
	 */
	public void destroy(String id) {
		ChangeOrder order = changeOrderMapper.getChangeOrderById(id);
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("id", id);
		condition.put("status", ChangeOrderStatus.DESTROY);
		condition.put("etime", new Date());
		
		changeOrderMapper.updateChangeOrderStatus(condition);
		//删除流程实例
		RuntimeService runtimeService = processEngine.getRuntimeService();
		runtimeService.deleteProcessInstance(order.getProcessInstanceId(), "待定");
		
		changeOrderMapper.updateProcessInstanceId(null, id);
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		map.put("id", id);
		map.put("isClaimed", "N");
		
		changeOrderMapper.updateChangeOrderIsClaimed(map);
	}
	
	/**
	 * 更新是否签收
	 */
	public int updateChangeOrderIsClaimed(Map<String, Object> variables) {
		
		Map<String, Object> condition = new HashMap<String, Object>();
		
		condition.put("id", variables.get("id"));
		condition.put("isClaimed", "Y");
		return changeOrderMapper.updateChangeOrderIsClaimed(condition);
	}
	
	/**
	 * 流程取回
	 */
	public void reset (String id) {
		ChangeOrder order = this.getChangeOrderById(id);
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("id", id);
		condition.put("status", ChangeOrderStatus.WTJ);
		condition.put("isClaimed", "N");//设置是否被签收
		changeOrderMapper.updateChangeOrderStatus(condition);
		//删除流程
		RuntimeService runtimeService = processEngine.getRuntimeService();
		runtimeService.deleteProcessInstance(order.getProcessInstanceId(), "待定");
		
		changeOrderMapper.updateProcessInstanceId(null, id);
	}
}
