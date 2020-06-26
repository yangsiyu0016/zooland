package com.zoo.service.erp.assembled;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.util.StringUtil;
import com.zoo.controller.erp.constant.AssembledStatus;
import com.zoo.controller.erp.constant.ProductAssembledStatus;
import com.zoo.controller.erp.constant.SellStatus;
import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.assembled.ProductAssembledMapper;
import com.zoo.mapper.erp.assembled.ProductAssembledMaterialMapper;
import com.zoo.model.erp.assembled.ProductAssembled;
import com.zoo.model.erp.assembled.ProductAssembledMaterial;
import com.zoo.model.system.user.UserInfo;
import com.zoo.service.system.parameter.SystemParameterService;
import com.zoo.utils.CodeGenerator;

@Service
@Transactional
public class ProductAssembledService {
	@Autowired
	ProductAssembledMapper paMapper;
	@Autowired
	SystemParameterService systemParameterService;
	@Autowired
	ProductAssembledMaterialMapper pamMapper;
	@Autowired
	IdentityService identityService;
	@Autowired
	ProcessEngine processEngine;
	@Autowired
	TaskService taskService;
	public List<ProductAssembled> getProductAssembledByPage(Integer page, Integer size,String keywords,
			String code,String productCode,String productName,String status,String warehouseId,
			String start_assembledTime,String end_assembledTime,String start_ctime,String end_ctime,String sort,String order) {
		Integer start = (page-1)*size;
		List<ProductAssembled> productAssembleds = paMapper.getProductAssembledByPage(start,size,keywords,code,productCode,productName,status,warehouseId,start_assembledTime,end_assembledTime,start_ctime,end_ctime,LoginInterceptor.getLoginUser().getCompanyId(),sort,order);
		return productAssembleds;
	}
	public long getCount(String keywords,
			String code,String productCode,String productName,String status,String warehouseId,
			String start_assembledTime,String end_assembledTime,String start_ctime,String end_ctime) {
		// TODO Auto-generated method stub
		return paMapper.getCount(keywords,code,productCode,productName,status,warehouseId,start_assembledTime,end_assembledTime,start_ctime,end_ctime,LoginInterceptor.getLoginUser().getCompanyId());
	}
	public ProductAssembled getProductAssembledById(String id) {
		return paMapper.getProductAssembledById(id);
	}
	public void addProductAssembled(ProductAssembled productAssembled) {
		String id = UUID.randomUUID().toString();
		productAssembled.setId(id);
		if(productAssembled.getCodeGeneratorType().equals("AUTO")) {
			try {
				String parameterValue = systemParameterService.getValueByCode("C00006");
				String code = CodeGenerator.getInstance().generator(parameterValue);
				productAssembled.setCode(code);
			} catch (Exception e) {
				throw new ZooException(ExceptionEnum.GENER_CODE_ERROR);
			}
		}
		productAssembled.setCuserId(LoginInterceptor.getLoginUser().getId());
		productAssembled.setCtime(new Date());
		productAssembled.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		productAssembled.setStatus(AssembledStatus.WTJ);
		paMapper.addProductAssembled(productAssembled);
		
		for(ProductAssembledMaterial material:productAssembled.getMaterials()) {
			material.setId(UUID.randomUUID().toString());
			material.setProductAssembledId(id);
			pamMapper.addMaterial(material);
		}
	}
	public void updateProductAssembled(ProductAssembled productAssembled) {

		paMapper.updateProductAssembled(productAssembled);
		pamMapper.deleteMaterialByAllembledId(productAssembled.getId());
		for(ProductAssembledMaterial material:productAssembled.getMaterials()) {
			material.setId(UUID.randomUUID().toString());
			material.setProductAssembledId(productAssembled.getId());
			pamMapper.addMaterial(material);
		}
	}
	public void deleteProductAssembledById(String ids) {
		String[] split = ids.split(",");
		for(String productAssembledId:split) {
			//删除材料
			pamMapper.deleteMaterialByAllembledId(productAssembledId);

		}
		paMapper.deleteProductAssembledById(split);
		
	}
	public void startProcess(String id) {
		ProductAssembled pa = this.getProductAssembledById(id);
		if(StringUtil.isNotEmpty(pa.getProcessInstanceId())) {
			throw new ZooException(ExceptionEnum.FLOWSTATED);
		}
		UserInfo user = LoginInterceptor.getLoginUser();
		Map<String, Object> variables=new HashMap<String,Object>();
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
        identityService.setAuthenticatedUserId(user.getId());
		String businessKey = id;
		variables.put("CODE", pa.getCode());
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance processInstance = runtimeService
        		.startProcessInstanceByKeyAndTenantId("assembled",businessKey,variables, user.getCompanyId());
        String processInstanceId = processInstance.getId();
        this.paMapper.updateProcessInstanceId(id, processInstanceId);
        Map<String,Object> condition = new HashMap<String,Object>();
        condition.put("id", id);
		condition.put("status", ProductAssembledStatus.ZGSH);
		this.updateProductAssembledStatus(condition);
	}
	private int updateProductAssembledStatus(Map<String, Object> condition) {
		return paMapper.updateProductAssembledStatus(condition);
		
	}
	//流程取回
	public void takeBack(String id) {
		// TODO Auto-generated method stub
		ProductAssembled pa = this.getProductAssembledById(id);
		Task task = taskService.createTaskQuery().processInstanceId(pa.getProcessInstanceId()).active().singleResult();
		if(task.getTaskDefinitionKey().equals("assembledckzg")) {
			if(StringUtil.isEmpty(task.getAssignee())) {
				Map<String,Object> condition = new HashMap<String, Object>();
				condition.put("id", id);
				condition.put("status", SellStatus.WTJ);

				paMapper.updateProductAssembledStatus(condition);
				//删除流程
				RuntimeService runtimeService = processEngine.getRuntimeService();
				runtimeService.deleteProcessInstance(pa.getProcessInstanceId(), "待定");
				
				paMapper.updateProcessInstanceId(id, null);
			}else {
				throw new ZooException("审批人已签收不能取回");
			}
		}else {
			throw new ZooException("当前节点不能取回");
		}
		
		
		
	}
}
