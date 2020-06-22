package com.zoo.service.erp.productsplit;

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

import com.zoo.controller.erp.constant.ProductSplitStatus;
import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.productsplit.ProductSplitDetailMapper;
import com.zoo.mapper.erp.productsplit.ProductSplitMapper;
import com.zoo.mapper.erp.warehouse.StockDetailMapper;
import com.zoo.mapper.erp.warehouse.StockMapper;
import com.zoo.model.erp.productsplit.ProductSplit;
import com.zoo.model.erp.productsplit.ProductSplitDetail;
import com.zoo.model.system.user.UserInfo;
import com.zoo.service.system.parameter.SystemParameterService;
import com.zoo.utils.CodeGenerator;

@Service
public class ProductSplitService {

	@Autowired
	private ProductSplitMapper productSplitMapper;
	
	@Autowired
	private ProductSplitDetailMapper detailMapper;
	
	@Autowired
	ProcessEngine processEngine;
	
	@Autowired
	IdentityService identityService;
	@Autowired
	StockMapper stockMapper;
	@Autowired
	StockDetailMapper stockDetailMapper;
	@Autowired
	SystemParameterService systemParameterService;
	
	/**
	 * 分页查询
	 * @param page
	 * @param size
	 * @return
	 */
	public List<ProductSplit> getProductSplitByPage(Integer page, Integer size){
		Integer start = null;
		if(page != null) {
			start = (page - 1) * size;
		}
		return productSplitMapper.getProductSplitByPage(start, size, LoginInterceptor.getLoginUser().getCompanyId());
	}
	
	/**
	 * 获取总条数
	 * @return
	 */
	public Long getCount() {
		return productSplitMapper.getCount(LoginInterceptor.getLoginUser().getCompanyId());
	}
	
	/**
	 * 根据id获取拆分单
	 * @param id
	 * @return
	 */
	public ProductSplit getProductSplitById(String id) {
		return productSplitMapper.getProductSplitById(id);
	}
	
	/**
	 * 更新拆分单
	 * @param productSplit
	 */
	public void updatePeoductSplit(ProductSplit productSplit) {
		productSplitMapper.updatePeoductSplit(productSplit);
		detailMapper.deleteByProductSplitId(productSplit.getId());
		for(ProductSplitDetail detail : productSplit.getDetails()) {
			detail.setId(UUID.randomUUID().toString());
			detail.setCtime(new Date());
			detail.setTotalNumber(detail.getNumber().multiply(productSplit.getNumber()));
			detail.setProductSplitId(productSplit.getId());
			detailMapper.addDetail(detail);
		}
	}
	
	public void deleteDetailById(String ids) {
		// TODO Auto-generated method stub
		String[] split = ids.split(",");
		productSplitMapper.deleteProductSplitByIds(split);
		for(String id: split) {
			detailMapper.deleteByProductSplitId(id);
		}
	}
	
	/**
	 * 添加拆分单
	 * @param productSplit
	 */
	public void addProductSplit(ProductSplit productSplit) {
		String id = UUID.randomUUID().toString();
		productSplit.setId(id);
		if("AUTO".equals(productSplit.getCodeGeneratorType())) {
			try {
				String parameterValue = systemParameterService.getValueByCode("c000012");
				String code = CodeGenerator.getInstance().generator(parameterValue);
				productSplit.setCode(code);
			} catch (Exception e) {
				// TODO: handle exception
				throw new ZooException(ExceptionEnum.GENER_CODE_ERROR);
			}
		}
		productSplit.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		productSplit.setSplitManId(LoginInterceptor.getLoginUser().getId());
		productSplit.setCtime(new Date());
		productSplit.setStatus(ProductSplitStatus.WTJ);
		productSplitMapper.addProductSplit(productSplit);
		for(ProductSplitDetail detail : productSplit.getDetails()) {
			detail.setId(UUID.randomUUID().toString());
			detail.setCtime(new Date());
			detail.setTotalNumber(detail.getNumber().multiply(productSplit.getNumber()));
			detail.setProductSplitId(id);
			detailMapper.addDetail(detail);
		}
	}
	
	/**
	 * 更改拆分单状态
	 * @param condition
	 */
	public void updateProductSplitStatus(Map<String, Object> condition) {
		productSplitMapper.updateProductSplitStatus(condition);
	}
	
	/**
	 * 开启流程
	 * @param id
	 */
	public void startProcess(String id) {
		ProductSplit split = this.getProductSplitById(id);
		UserInfo info = LoginInterceptor.getLoginUser();
		Map<String, Object> variables = new HashMap<String, Object>();
		
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		identityService.setAuthenticatedUserId(info.getId());
		
		String businessKey = id;
		
		variables.put("CODE", split.getCode());
		
		//启动流程
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId("inventoryCheck", businessKey, variables,info.getCompanyId());
		//获取流程id
		String processInstanceId = processInstance.getId();
		//更新到自定义拆分表中
		productSplitMapper.updateProcessInstanceId(id, processInstanceId);
		
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("id", id);
		condition.put("status", ProductSplitStatus.CKZG);
		productSplitMapper.updateProductSplitStatus(condition);
	}
	
	/**
	 * 更改签收状态
	 * @param variables
	 */
	public void updateProductSplitIsClaimed(Map<String, Object> variables) {
		Map<String, Object> condition = new HashMap<String, Object>();
		// TODO Auto-generated method stub
		
		condition.put("id", variables.get("id"));
		condition.put("isClaimed", "Y");
		
		productSplitMapper.updateProductSplitIsClaimed(condition);
	}
	
	/**
	 * 作废
	 * @param id
	 */
	public void destroy(String id) {
		Map<String,Object> condition = new HashMap<String,Object>();
		ProductSplit split = productSplitMapper.getProductSplitById(id);
		
		//更新订单表现状态
		condition.put("id", id);
		condition.put("status", ProductSplitStatus.DESTROY);
		condition.put("etime", new Date());
		productSplitMapper.updateProductSplitStatus(condition);
		
		//设置是否被签收表示
		Map<String,Object> isClaimedCondition = new HashMap<String,Object>();
		isClaimedCondition.put("id", split.getId());
		isClaimedCondition.put("isClaimed", "N");
		productSplitMapper.updateProductSplitIsClaimed(isClaimedCondition);
	}

}
