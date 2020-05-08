package com.zoo.service.erp.purchase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.util.StringUtil;
import com.zoo.controller.erp.constant.PurchaseStatus;
import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.purchase.PurchaseMapper;
import com.zoo.mapper.erp.product.SpecParamMapper;
import com.zoo.mapper.erp.purchase.PurchaseDetailMapper;
import com.zoo.model.erp.product.ProductSku;
import com.zoo.model.erp.product.SpecParam;
import com.zoo.model.erp.purchase.Purchase;
import com.zoo.model.erp.purchase.PurchaseDetail;
import com.zoo.model.erp.sell.SellDetail;
import com.zoo.model.system.user.UserInfo;
import com.zoo.utils.OrderCodeHelper;

import net.sf.json.JSONObject;

import org.activiti.engine.ProcessEngine;

@Service
@Transactional
public class PurchaseService {
	@Autowired
	PurchaseMapper purchaseMapper;
	@Autowired
	PurchaseDetailMapper detailMapper;
	@Autowired
	IdentityService identityService;
	@Autowired
	ProcessEngine processEngine;
	@Autowired
	SpecParamMapper paramMapper;
	public void addPurchase(Purchase purchase) {
		String id = UUID.randomUUID().toString();
		purchase.setId(id);
		if(purchase.getCodeGeneratorType().equals("AUTO")) {
			try {
				purchase.setCode(OrderCodeHelper.GenerateId("CG"));
			} catch (Exception e) {
				throw new ZooException(ExceptionEnum.GENER_CODE_ERROR);
			}
		}
		purchase.setStatus(PurchaseStatus.WTJ);
		purchase.setCuserId(LoginInterceptor.getLoginUser().getId());
		purchase.setCtime(new Date());
		purchase.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		purchase.setAccountContext(purchase.getSupplierAccount().getBankNumber()+" | "+purchase.getSupplierAccount().getBankName()+" | "+purchase.getSupplierAccount().getAccountName());
		purchaseMapper.addPurchase(purchase);
		
		for(PurchaseDetail detail:purchase.getDetails()) {
			detail.setId(UUID.randomUUID().toString());
			detail.setPurchaseId(id);
			detail.setCtime(new Date());
			detailMapper.addDetail(detail);
		}
	}

	public List<Purchase> getPurchaseByPage(Integer page, Integer size) {
		Integer start = null;
		if(page!=null) {
			start = (page-1)*size;
		}
		List<Purchase> purchases = purchaseMapper.getPurchaseByPage(start,size,LoginInterceptor.getLoginUser().getCompanyId());
		return purchases;
	}

	public long getCount() {
		// TODO Auto-generated method stub
		return purchaseMapper.getCount(LoginInterceptor.getLoginUser().getCompanyId());
	}

	public void updatePurchaseStatus(Map<String, Object> condition) {
		purchaseMapper.updatePurchaseStatus(condition);
		
	}

	public Purchase getPurchaseById(String id) {
		Purchase purchase = purchaseMapper.getPurchaseById(id);
		List<String> built = new ArrayList<String>();
		for(PurchaseDetail detail:purchase.getDetails()) {
			ProductSku sku = detail.getProductSku();
			if(!built.contains(sku.getProduct().getId())) {
				//通用规格参数处理
				String genericSpec = sku.getProduct().getProductDetail().getGenericSpec();
				Map<String,String> map = new HashMap<String,String>();
				JSONObject obj = JSONObject.fromObject(genericSpec);
				Set<String> keyset = obj.keySet();
				for(String key:keyset) {
					SpecParam param = paramMapper.getParamById(key);
					map.put(param.getName(), StringUtils.isBlank(obj.getString(key))?"其它":obj.getString(key));
				}
				sku.getProduct().getProductDetail().setGenericSpec(map.toString());
				
				
				String ownSpec = sku.getOwnSpec();
				 map = new HashMap<String,String>();
				 obj  = JSONObject.fromObject(ownSpec);
				keyset = obj.keySet();
				for(String key:keyset) {
					SpecParam param = paramMapper.getParamById(key);
					map.put(param.getName(), obj.getString(key));
				}
				sku.setOwnSpec(map.toString());
				
				detail.setProductSku(sku);
				built.add(sku.getProduct().getId());
			}
			
		}
		return purchase;
	}

	public void startProcess(String id) {
		Purchase purchase = this.getPurchaseById(id);
		if(StringUtil.isNotEmpty(purchase.getProcessInstanceId())) {
			throw new ZooException(ExceptionEnum.FLOWSTATED);
		}
		UserInfo user = LoginInterceptor.getLoginUser();
		Map<String, Object> variables=new HashMap<String,Object>();
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
        identityService.setAuthenticatedUserId(user.getId());
		String businessKey = id;
		variables.put("CODE", purchase.getCode());
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance processInstance = runtimeService
        		.startProcessInstanceByKeyAndTenantId("purchase",businessKey,variables, user.getCompanyId());
        String processInstanceId = processInstance.getId();
        this.purchaseMapper.updateProcessInstanceId(id, processInstanceId);
        Map<String,Object> condition = new HashMap<String,Object>();
        condition.put("id", id);
		condition.put("status", PurchaseStatus.CGJLSH);
		this.updatePurchaseStatus(condition);
	}

	public void updatePurchase(Purchase purchase) {
		purchase.setAccountContext(purchase.getSupplierAccount().getBankNumber()+" | "
					+purchase.getSupplierAccount().getBankName()+" | "
					+purchase.getSupplierAccount().getAccountName());
		purchaseMapper.updatePurchase(purchase);
	}

	public void destroy(String id) {
		Purchase purchase = purchaseMapper.getPurchaseById(id);
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("id", id);
		condition.put("status", PurchaseStatus.DESTROY);
		condition.put("etime", new Date());
		purchaseMapper.updatePurchaseStatus(condition);
		//删除流程实例
		RuntimeService runtimeService = processEngine.getRuntimeService();
		runtimeService.deleteProcessInstance(purchase.getProcessInstanceId(),"待定");
		
		purchaseMapper.updateProcessInstanceId(id, null);
	}

}
