package com.zoo.service.erp.inventorycheck;

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
import org.springframework.transaction.annotation.Transactional;

import com.zoo.controller.erp.constant.InventoryCheckStatus;
import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.inventorycheck.InventoryCheckDetailMapper;
import com.zoo.mapper.erp.inventorycheck.InventoryCheckMapper;
import com.zoo.mapper.erp.product.SpecParamMapper;
import com.zoo.model.erp.inventorycheck.InventoryCheck;
import com.zoo.model.erp.inventorycheck.InventoryCheckDetail;
import com.zoo.model.erp.product.ProductSku;
import com.zoo.model.erp.product.SpecParam;
import com.zoo.model.system.user.UserInfo;
import com.zoo.utils.OrderCodeHelper;

import ch.qos.logback.core.status.Status;
import net.sf.json.JSONObject;

@Service
@Transactional
public class InventoryCheckService {
	
	@Autowired
	private InventoryCheckMapper inventoryCheckMapper;
	
	@Autowired
	private SpecParamMapper paramMapper;
	
	@Autowired
	private InventoryCheckDetailMapper detailMapper;
	
	@Autowired
	ProcessEngine processEngine;
	
	@Autowired
	IdentityService identityService;
	
	//分页查询
	public List<InventoryCheck> getInventoryCheckByPage(Integer page, Integer size, String cuserId) {
		int start = (page - 1) * size;
		
		String companyId = LoginInterceptor.getLoginUser().getCompanyId();
		
		List<InventoryCheck> inventoryChecks = inventoryCheckMapper.getInventoryCheckByPage(start, size, companyId, cuserId);
		
		return inventoryChecks;
		
	}

	//查询总数
	public Long getCount(String cuserId) { 
		Long count = inventoryCheckMapper.getCount(LoginInterceptor.getLoginUser().getCompanyId(), cuserId);
		
		return count;
	}

	//根据id获取单个数据
	public InventoryCheck getInventoryCheckById(String id) {
		InventoryCheck ic = inventoryCheckMapper.getInventoryCheckById(id);
		
		return buildSpec(ic);
	}
	
	//添加数据
	public void addInventoryCheck(InventoryCheck ic) {
		String id = UUID.randomUUID().toString();
		ic.setId(id);
		if("AUTO".equals(ic.getCodeGeneratorType())) {
			try {
				ic.setCode(OrderCodeHelper.GenerateId("QC"));
			} catch (Exception e) {
				// TODO: handle exception
				throw new ZooException(ExceptionEnum.GENER_CODE_ERROR);
			}
		}
		ic.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		ic.setCuserId(LoginInterceptor.getLoginUser().getId());
		ic.setStatus(InventoryCheckStatus.WTJ);
		ic.setCtime(new Date());
		inventoryCheckMapper.addInventoryCheck(ic);
		for(InventoryCheckDetail detail : ic.getDetails()) {
			detail.setId(UUID.randomUUID().toString());
			detail.setCtime(new Date());
			detail.setPanDianId(id);
			detailMapper.addDetail(detail);
		}
	}
	
	//更新订单状态
	public int updateInventoryCheckStatus(Map<String, Object> condition) {
		return this.inventoryCheckMapper.updateInventoryCheckStatus(condition);
	}
	
	//更新数据
	public int updateInventoryCheck(InventoryCheck ic) {
		int num = inventoryCheckMapper.updateInventoryCheck(ic);
		return num;
	}
	
	//流程开启
	public void startProcess(String id) {
		InventoryCheck ic = this.getInventoryCheckById(id);
		
		UserInfo user = LoginInterceptor.getLoginUser();
		Map<String,Object> variables = new HashMap<String, Object>();
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(user.getId());
		
		String businessKey = id;
		
		variables.put("CODE", ic.getCode());
		//启动流程
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId("inventoryCheck", businessKey, variables,user.getCompanyId());
		//获取流程id
		String processInstanceId = processInstance.getId();
		//更新到自定义盘点表中
		this.inventoryCheckMapper.updateProcessInstanceId(id, processInstanceId);
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("id", id);
		condition.put("status", InventoryCheckStatus.ZGSH);
		this.inventoryCheckMapper.updateInventoryCheckStatus(condition);
		
	}
	
	public void doCallBackFlow(String id) {
		InventoryCheck ic = this.getInventoryCheckById(id);
		Map<String,Object> condition = new HashMap<String,Object>();
		
		condition.put("id", id);
		condition.put("status", InventoryCheckStatus.WTJ);
		this.updateInventoryCheckStatus(condition);
		inventoryCheckMapper.updateProcessInstanceId(id, null);
		RuntimeService runtimeService = processEngine.getRuntimeService();
		runtimeService.deleteProcessInstance(ic.getProcessInstanceId(), "待定");
	}
	
/*-----------------------------------------------------------------------------------------------*/	
	
	//创建规格参数信息
	private InventoryCheck buildSpec(InventoryCheck ic) {
		
		List<String> built = new ArrayList<String>();
		
		for(InventoryCheckDetail detail : ic.getDetails()) {
			ProductSku sku = detail.getProductSku();
			
			if(!built.contains(sku.getProduct().getId())) {
				//通用规格参数处理
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
				map = new HashMap<String,String>();
				obj  = JSONObject.fromObject(ownSpec);
				keySet = obj.keySet();
				for(String key:keySet) {
					SpecParam param = paramMapper.getParamById(key);
					map.put(param.getName(), obj.getString(key));
				}
				sku.setOwnSpec(map.toString());
				
				detail.setProductSku(sku);
				built.add(sku.getProduct().getId());
			}
		}
		return ic;
	}
}
