package com.zoo.service.erp.purchase;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.util.StringUtil;
import com.zoo.controller.erp.constant.JournalAccountType;
import com.zoo.controller.erp.constant.PurchaseStatus;
import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.purchase.PurchaseMapper;
import com.zoo.mapper.annex.AnnexMapper;
import com.zoo.mapper.erp.purchase.PurchaseDetailMapper;
import com.zoo.model.annex.Annex;
import com.zoo.model.erp.JournalAccount;
import com.zoo.model.erp.cost.Cost;
import com.zoo.model.erp.cost.CostDetail;
import com.zoo.model.erp.cost.CostDetailGoodsAllocation;
import com.zoo.model.erp.purchase.Purchase;
import com.zoo.model.erp.purchase.PurchaseDetail;
import com.zoo.model.erp.warehouse.Stock;
import com.zoo.model.erp.warehouse.StockDetail;
import com.zoo.model.system.user.UserInfo;
import com.zoo.service.annex.AnnexService;
import com.zoo.service.erp.JournalAccountService;
import com.zoo.service.erp.cost.CostService;
import com.zoo.service.erp.inbound.InboundService;
import com.zoo.service.erp.warehouse.StockDetailService;
import com.zoo.service.erp.warehouse.StockService;
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
	@Autowired
	TaskService taskService;
	@Autowired
	CostService costService;
	@Autowired
	StockService stockService;
	@Autowired
	StockDetailService stockDetailService;
	@Autowired
	JournalAccountService journalAccountService;
	@Autowired
	InboundService inboundService;
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

	public List<Purchase> getPurchaseByPage(
			Integer page, Integer size,
			String cuserId,String keywords,
			String code,String productCode,
			String productName,String supplierName,
			String start_initDate,String end_initDate,
			String start_ctime,String end_ctime,
			String status,String sort,String order) {
		Integer start = null;
		if(page!=null) {
			start = (page-1)*size;
		}
		List<Purchase> purchases = purchaseMapper.getPurchaseByPage(
				start,size,
				LoginInterceptor.getLoginUser().getCompanyId(),cuserId,
				keywords,code,
				productCode,productName,
				supplierName,start_initDate,end_initDate,start_ctime,end_ctime,status.length()>0?status.split(","):null,sort,order);
		return purchases;
	}

	public long getCount(String cuserId,String keywords,
			String code,String productCode,
			String productName,String supplierName,
			String start_initDate,String end_initDate,
			String start_ctime,String end_ctime,
			String status) {
		// TODO Auto-generated method stub
		return purchaseMapper.getCount(LoginInterceptor.getLoginUser().getCompanyId(),cuserId,keywords,code,
				productCode,productName,
				supplierName,start_initDate,end_initDate,start_ctime,end_ctime,status.split(","));
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
		
		List<Cost> costs = costService.getCostByForeignKey(id);
		for(Cost cost:costs) {
			if(cost.getFinished()) {
				List<CostDetail> costDetails = cost.getDetails();
				for(CostDetail costDetail:costDetails) {
					Stock stock = stockService.getStock(costDetail.getProduct().getId(), cost.getWarehouse().getId());
					if(stock.getUsableNumber().subtract(costDetail.getNumber()).compareTo(BigDecimal.ZERO)==-1) {//库存不足
						throw new ZooException(costDetail.getProduct().getName()+"库存不足，不能作废");
					}else {
						List<CostDetailGoodsAllocation> cdgas = costDetail.getCdgas();
						for(CostDetailGoodsAllocation cdga:cdgas) {
							StockDetail stockDetail = stockDetailService.getStockDetail(stock.getId(), cdga.getGoodsAllocation().getId());
							if(stockDetail.getUsableNumber().subtract(cdga.getNumber()).compareTo(BigDecimal.ZERO)==-1) {//货位库存不足
								throw new ZooException(costDetail.getProduct().getName()+"在货位:"+cdga.getGoodsAllocation().getName()+"上库存不足");
							}else {
								stockDetail.setUsableNumber(stockDetail.getUsableNumber().subtract(cdga.getNumber()));
								stockDetailService.updateStockDetail(stockDetail);
							}
						}
						BigDecimal after_usableNumber = stock.getUsableNumber().subtract(costDetail.getNumber());
						BigDecimal after_totalMoney;
						BigDecimal after_costPrice;
						if(after_usableNumber.compareTo(BigDecimal.ZERO)==1) {
							after_totalMoney = stock.getTotalMoney().subtract(costDetail.getTotalMoney());
							after_costPrice = after_totalMoney.divide(after_usableNumber,4,BigDecimal.ROUND_HALF_UP);
							
						}else {
							after_totalMoney = new BigDecimal("0");
							after_costPrice = stock.getCostPrice();
						}
						stock.setUsableNumber(after_usableNumber);
						stock.setCostPrice(after_costPrice);
						stock.setTotalMoney(after_totalMoney);
						stockService.updateStock(stock);
						
						JournalAccount journalAccount = new JournalAccount();
						journalAccount.setId(UUID.randomUUID().toString());
						journalAccount.setType(JournalAccountType.PURCHASEDESTROY);
						journalAccount.setOrderDetailId(costDetail.getDetailId());
						journalAccount.setOrderCode(purchase.getCode());
						journalAccount.setStock(stock);
						journalAccount.setCkNumber(costDetail.getNumber());
						journalAccount.setCkPrice(costDetail.getPrice());
						journalAccount.setCkTotalMoney(costDetail.getTotalMoney());
						journalAccount.setCtime(new Date());
						journalAccount.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
						journalAccount.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
						journalAccountService.addJournalAccount(journalAccount);

					}
					
				}
			}
			
		}
		
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("id", id);
		condition.put("status", PurchaseStatus.DESTROY);
		condition.put("etime", new Date());
		purchaseMapper.updatePurchaseStatus(condition);
		//删除流程实例
		if(StringUtil.isNotEmpty(purchase.getProcessInstanceId())) {
			RuntimeService runtimeService = processEngine.getRuntimeService();
			runtimeService.deleteProcessInstance(purchase.getProcessInstanceId(),"待定");
			
			purchaseMapper.updateProcessInstanceId(id, null);
		}
		
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
		Task task = taskService.createTaskQuery().processInstanceId(purchase.getProcessInstanceId()).active().singleResult();
		if(task==null) throw new ZooException("任务不存在");
		if(task.getTaskDefinitionKey().equals("purchasecgjl")) {
			if(StringUtil.isEmpty(task.getAssignee())) {
				Map<String,Object> condition = new HashMap<String, Object>();
				condition.put("id", id);
				condition.put("status", PurchaseStatus.WTJ);
				condition.put("isClaimed", "N");//设置是否签收
				purchaseMapper.updatePurchaseStatus(condition);
				
				//删除流程
				RuntimeService runtimeService = processEngine.getRuntimeService();
				runtimeService.deleteProcessInstance(purchase.getProcessInstanceId(), "待定");
				
				purchaseMapper.updateProcessInstanceId(id, null);
			}else {
				throw new ZooException("审批人已签收不能取回");
			}
		}else {
			throw new ZooException("当前节点不能取回");
		}
		
		
	}

	public void deletePurchaseById(String ids) {
		String[] split = ids.split(",");
		for(String purchaseId:split) {
			Purchase purchase = this.getPurchaseById(purchaseId);
			if(StringUtil.isNotEmpty(purchase.getProcessInstanceId())&&!purchase.getStatus().equals(PurchaseStatus.DESTROY)) {
				throw new ZooException("流程已启动,不能删除");
			}
			//删除产品详情
			detailMapper.deleteDetailByPurchaseId(purchaseId);
			//删除物流信息
			costService.deleteByForeignKey(purchaseId);
			//删除附件
			annexService.delAnnexByForeignKey(purchaseId);
			inboundService.deleteInboundByForeignKey(purchaseId);
		}
		purchaseMapper.deletePurchaseById(split);
		
	}

}
