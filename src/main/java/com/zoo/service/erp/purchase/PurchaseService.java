package com.zoo.service.erp.purchase;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.util.StringUtil;
import com.zoo.controller.erp.constant.PurchaseStatus;
import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.purchase.PurchaseMapper;
import com.zoo.mapper.annex.AnnexMapper;
import com.zoo.mapper.erp.purchase.PurchaseDetailMapper;
import com.zoo.model.annex.Annex;
import com.zoo.model.erp.purchase.Purchase;
import com.zoo.model.erp.purchase.PurchaseDetail;
import com.zoo.model.system.user.UserInfo;
import com.zoo.service.annex.AnnexService;
import com.zoo.service.system.parameter.SystemParameterService;
import com.zoo.utils.CodeGenerator;
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
	AnnexMapper annexMapper;
	@Autowired
	AnnexService annexService;
	@Autowired
	SystemParameterService systemParameterService;
	public void addPurchase(Purchase purchase) {
		String id = UUID.randomUUID().toString();
		purchase.setId(id);
		if(purchase.getCodeGeneratorType().equals("AUTO")) {
			try {
				String parameterValue = systemParameterService.getValueByCode("C00004");
				String code = CodeGenerator.getInstance().generator(parameterValue);
				purchase.setCode(code);
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
		List<Annex> annexs = purchase.getAnnexs();
		for(Annex annex: annexs) {
			annex.setForeignKey(id);
			//将业务id添加到附件得业务id字段
			annexMapper.updateAnnexForeignKeyById(annex);
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
		List<Annex> annexs = purchase.getAnnexs();
		for(Annex annex: annexs) {
			annex.setForeignKey(purchase.getId());
			//将业务id添加到附件得业务id字段
			annexMapper.updateAnnexForeignKeyById(annex);
		}
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
		
		//设置是否被签收表示
		Map<String,Object> isClaimedCondition = new HashMap<String,Object>();
		isClaimedCondition.put("code", purchase.getCode());
		isClaimedCondition.put("isClaimed", "N");
		
		purchaseMapper.updatePurchaseIsClaimed(isClaimedCondition);
	}

	public void updatePurchaseIsClaimed(Map<String, Object> variables) {
		// TODO Auto-generated method stub
		Map<String, Object> condition = new HashMap<String, Object>();
		// TODO Auto-generated method stub
		
		condition.put("id", variables.get("id"));
		condition.put("isClaimed", "Y");
		purchaseMapper.updatePurchaseIsClaimed(condition);
	}
	
	//流程取回
	public void reset(String id) {
		// TODO Auto-generated method stub
		Purchase purchase = this.getPurchaseById(id);
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("id", id);
		condition.put("status", PurchaseStatus.WTJ);
		condition.put("isClaimed", "N");//设置是否签收
		purchaseMapper.updatePurchaseStatus(condition);
		
		//删除流程
		RuntimeService runtimeService = processEngine.getRuntimeService();
		runtimeService.deleteProcessInstance(purchase.getProcessInstanceId(), "待定");
		
		purchaseMapper.updateProcessInstanceId(id, null);
	}

	public void deletePurchaseById(String ids) {
		String[] split = ids.split(",");
		for(String purchaseId:split) {
			//删除产品详情
			detailMapper.deleteDetailByPurchaseId(purchaseId);
			//删除物流信息
			//costService.deleteByForeignKey(sellId);
			//删除附件
			annexService.delAnnexByForeignKey(purchaseId);
		}
		purchaseMapper.deletePurchaseById(split);
		
	}

}
