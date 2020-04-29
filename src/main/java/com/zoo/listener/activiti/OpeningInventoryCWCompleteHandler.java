package com.zoo.listener.activiti;


import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import com.zoo.controller.erp.constant.JournalAccountType;
import com.zoo.controller.erp.constant.OpeningInventoryStatus;
import com.zoo.filter.LoginInterceptor;
import com.zoo.model.erp.JournalAccount;
import com.zoo.model.erp.openingInventory.OpeningInventory;
import com.zoo.model.erp.openingInventory.OpeningInventoryDetail;
import com.zoo.model.erp.warehouse.Stock;
import com.zoo.model.erp.warehouse.StockDetail;
import com.zoo.service.erp.JournalAccountService;
import com.zoo.service.erp.openingInventory.OpeningInventoryService;
import com.zoo.service.erp.warehouse.StockDetailService;
import com.zoo.service.erp.warehouse.StockService;
import com.zoo.utils.ApplicationUtil;

/**
 * 财务审核完成
 * @author 52547
 *
 */
@Component("openingInventoryCWCompleteHandler")
public class OpeningInventoryCWCompleteHandler implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private ProcessEngine processEngine;
	private OpeningInventoryService openingInventoryService;
	//private OpeningInventoryDetailService openingInventoryDetailService;
	private StockService stockService;
	private StockDetailService stockDetailService;
	private JournalAccountService journalAccountService;
	@Override
	public void notify(DelegateTask delegateTask) {
		processEngine = (ProcessEngine) ApplicationUtil.getBean("processEngine");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		openingInventoryService = (OpeningInventoryService) ApplicationUtil.getBean("openingInventoryService");
		//openingInventoryDetailService = (OpeningInventoryDetailService) ApplicationUtil.getBean("openingInventoryDetailService");
		stockService = (StockService)ApplicationUtil.getBean("stockService");
		stockDetailService = (StockDetailService)ApplicationUtil.getBean("stockDetailService");
		journalAccountService = (JournalAccountService)ApplicationUtil.getBean("journalAccountService");
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(delegateTask.getProcessInstanceId()).singleResult();
		String key = pi.getBusinessKey();
		OpeningInventory openingInventory= openingInventoryService.getOpeningInventoryById(key);
		//Map<String,Object> condition = new HashMap<String,Object>();
		//condition.put("openingInventoryId", openingInventory.getId());
		List<OpeningInventoryDetail> details = openingInventory.getDetails();
		String warehouseId = openingInventory.getWarehouse().getId();
		for(OpeningInventoryDetail detail:details){
			Stock stock = stockService.getStock(detail.getProductSku().getId(), warehouseId);
			if(stock!=null){
				stock.setUsableNumber(stock.getUsableNumber().add(detail.getNumber()));
				stock.setTotalMoney(stock.getTotalMoney().add(detail.getTotalMoney()));
				stockService.updateStock(stock);
				StockDetail stockDetail = stockDetailService.getStockDetail(stock.getId(), detail.getGoodsAllocation().getId());
				if(stockDetail!=null){
					stockDetail.setUsableNumber(stockDetail.getUsableNumber().add(detail.getNumber()));
					stockDetailService.updateStockDetail(stockDetail);
				}else{
					stockDetail = new StockDetail();
					stockDetail.setId(UUID.randomUUID().toString());
					stockDetail.setStockId(stock.getId());
					stockDetail.setGoodsAllocation(detail.getGoodsAllocation());
					stockDetail.setUsableNumber(detail.getNumber());
					stockDetailService.addStockDetail(stockDetail);
				}
			}else{
				stock = new Stock();
				stock.setId(UUID.randomUUID().toString());
				stock.setUsableNumber(detail.getNumber());
				stock.setCostPrice(detail.getCostPrice());
				stock.setTotalMoney(detail.getNumber().multiply(detail.getCostPrice()));
				stock.setProductSku(detail.getProductSku());
				stock.setWarehouse(openingInventory.getWarehouse());
				stockService.addStock(stock);
				
				StockDetail stockDetail = new StockDetail();
				stockDetail.setId(UUID.randomUUID().toString());
				stockDetail.setStockId(stock.getId());
				stockDetail.setGoodsAllocation(detail.getGoodsAllocation());
				stockDetail.setUsableNumber(detail.getNumber());
				stockDetailService.addStockDetail(stockDetail);
			}
			JournalAccount journalAccount = new JournalAccount();
			journalAccount.setId(UUID.randomUUID().toString());
			journalAccount.setType(JournalAccountType.QC);
			journalAccount.setOrderDetailId(detail.getId());
			journalAccount.setOrderCode(openingInventory.getCode());
			journalAccount.setStock(stock);
			journalAccount.setRkNumber(detail.getNumber());
			journalAccount.setRkPrice(detail.getCostPrice());
			journalAccount.setRkTotalMoney(detail.getTotalMoney());
			journalAccount.setCtime(new Date());
			journalAccount.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
			journalAccount.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
			journalAccountService.addJournalAccount(journalAccount);
		}
	
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("id", openingInventory.getId());
		condition.put("status", OpeningInventoryStatus.FINISHED);
		condition.put("etime", new Date());
		openingInventoryService.updateOpeningInventoryStatus(condition);
	}
	
}
