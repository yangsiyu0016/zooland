package com.zoo.listener.activiti.inventorycheck;

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

import com.zoo.controller.erp.constant.InventoryCheckStatus;
import com.zoo.controller.erp.constant.InventoryDetailType;
import com.zoo.controller.erp.constant.JournalAccountType;
import com.zoo.filter.LoginInterceptor;
import com.zoo.model.erp.JournalAccount;
import com.zoo.model.erp.inventorycheck.InventoryCheck;
import com.zoo.model.erp.inventorycheck.InventoryCheckDetail;
import com.zoo.model.erp.warehouse.Stock;
import com.zoo.model.erp.warehouse.StockDetail;
import com.zoo.service.erp.JournalAccountService;
import com.zoo.service.erp.inventorycheck.InventoryCheckService;
import com.zoo.service.erp.warehouse.StockDetailService;
import com.zoo.service.erp.warehouse.StockService;
import com.zoo.utils.ApplicationUtil;

@Component("inventoryCheckGXKCCompleteHandler")
public class InventoryCheckGXKCCompleteHandler implements TaskListener{
	private ProcessEngine processEngine;
	private StockService stockService;
	private StockDetailService stockDetailService;
	private InventoryCheckService inventoryCheckService;
	private JournalAccountService journalAccountService;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	@Override
	public void notify( DelegateTask delegateTask) {
		processEngine = (ProcessEngine) ApplicationUtil.getBean("processEngine");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance result = runtimeService.createProcessInstanceQuery().processInstanceId(delegateTask.getProcessInstanceId()).singleResult();
		String key = result.getBusinessKey();
		stockService = (StockService)ApplicationUtil.getBean("stockService");
		stockDetailService = (StockDetailService)ApplicationUtil.getBean("stockDetailService");
		inventoryCheckService = (InventoryCheckService)ApplicationUtil.getBean("inventoryCheckService");
		journalAccountService = (JournalAccountService)ApplicationUtil.getBean("journalAccountService");
		InventoryCheck check = inventoryCheckService.getInventoryCheckById(key);
		String warehouseId = check.getWarehouse().getId();
		List<InventoryCheckDetail> details = check.getDetails();
		for(InventoryCheckDetail detail:details) {
			String productId = detail.getProduct().getId();
			//1、查询库存
			Stock stock = stockService.getStock(productId, warehouseId);
			
			//获取货位库存信息
			StockDetail stockDetail = stockDetailService.getStockDetail(stock.getId(), detail.getGoodsAllocation().getId());
			
			if(detail.getType().equals(InventoryDetailType.LOSSES)) {
				//更新货位库存数量 
				stockDetail.setUsableNumber(stockDetail.getUsableNumber().subtract(detail.getNumber()));
				stockDetailService.updateStockDetail(stockDetail);
				//变化后库存总额
				BigDecimal after_totalMoney = stock.getTotalMoney().subtract(detail.getTotalMoney());
				//变化后可用库存
				BigDecimal after_usableNumber = stock.getUsableNumber().subtract(detail.getNumber());
				//变化后总库存
				BigDecimal after_totalNumber = after_usableNumber.add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber());
				
				//变化后成本价
				BigDecimal after_costPrice = stock.getCostPrice();
				if(after_totalNumber.compareTo(BigDecimal.ZERO)==0) {
					after_totalMoney = new BigDecimal("0");
				}else {
					after_costPrice = after_totalMoney.divide(after_totalNumber,4,BigDecimal.ROUND_HALF_UP);
				}
				stock.setCostPrice(after_costPrice);
				stock.setTotalMoney(after_totalMoney);
				stock.setUsableNumber(after_usableNumber);
				stockService.updateStock(stock);
				
				
				JournalAccount journalAccount = new JournalAccount();
				journalAccount.setId(UUID.randomUUID().toString());
				journalAccount.setType(JournalAccountType.LOSSES);
				journalAccount.setOrderDetailId(detail.getId());
				journalAccount.setOrderCode(check.getCode());
				journalAccount.setStock(stock);
				journalAccount.setCkNumber(detail.getNumber());
				journalAccount.setCkPrice(detail.getCostPrice());
				journalAccount.setCkTotalMoney(detail.getTotalMoney());
				journalAccount.setCtime(new Date());
				journalAccount.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
				journalAccount.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
				journalAccountService.addJournalAccount(journalAccount);
				
				
			}else {//盘盈
				
				if(stock != null) {//如果库存存在
					//变化后库存成本总额
					BigDecimal after_money = stock.getTotalMoney().add(detail.getTotalMoney());
					//变化后可用库存
					BigDecimal after_usableNumber = stock.getUsableNumber().add(detail.getNumber());
					//变化后总库存
					BigDecimal after_totalNumber = after_usableNumber.add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber());
					
					//变化后成本价
					BigDecimal after_costPrice = stock.getCostPrice();
					if(after_totalNumber.compareTo(BigDecimal.ZERO)==0) {
						after_money = new BigDecimal("0");
					}else {
						after_costPrice = after_money.divide(after_totalNumber,4,BigDecimal.ROUND_HALF_UP);
					}
					
					stock.setCostPrice(after_costPrice);
					stock.setTotalMoney(after_money);
					stock.setUsableNumber(after_usableNumber);
					
					stockService.updateStock(stock);
					if(stockDetail != null) {//如果货位存在
						//更新货位库存数量
						stockDetail.setUsableNumber(stockDetail.getUsableNumber().add(detail.getNumber()));
						stockDetailService.updateStockDetail(stockDetail);
						stockDetailService.updateStockDetail(stockDetail);
					}else {//如果货位不存在
						stockDetail = new StockDetail();
						stockDetail.setId(UUID.randomUUID().toString());
						stockDetail.setStockId(stock.getId());
						stockDetail.setGoodsAllocation(detail.getGoodsAllocation());
						stockDetail.setUsableNumber(detail.getNumber());
						stockDetailService.addStockDetail(stockDetail);
					}
				}else {//不存在
					stock = new Stock();
					stock.setId(UUID.randomUUID().toString());
					stock.setUsableNumber(detail.getNumber());
					stock.setCostPrice(detail.getCostPrice());
					stock.setTotalMoney(detail.getNumber().multiply(detail.getCostPrice()));
					stock.setProduct(detail.getProduct());
					stock.setWarehouse(check.getWarehouse());
					stockService.addStock(stock);
					
					stockDetail = new StockDetail();
					stockDetail.setId(UUID.randomUUID().toString());
					stockDetail.setStockId(stock.getId());
					stockDetail.setGoodsAllocation(detail.getGoodsAllocation());
					stockDetail.setUsableNumber(detail.getNumber());
					stockDetailService.addStockDetail(stockDetail);
				}
				
				//库存变化
				JournalAccount journalAccount = new JournalAccount();
				journalAccount.setId(UUID.randomUUID().toString());
				journalAccount.setType(JournalAccountType.OVERFLOW);
				journalAccount.setOrderDetailId(detail.getId());
				journalAccount.setOrderCode(check.getCode());
				journalAccount.setStock(stock);
				journalAccount.setRkNumber(detail.getNumber());
				journalAccount.setRkPrice(detail.getCostPrice());
				journalAccount.setRkTotalMoney(detail.getTotalMoney());
				journalAccount.setCtime(new Date());
				journalAccount.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
				journalAccount.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
				 
				journalAccountService.addJournalAccount(journalAccount);
				
			}	
		}
		
		//更新订单状态
		Map<String,Object> condition = new HashMap<String,Object>();
		
		
		condition = new HashMap<String,Object>();
		condition.put("id", check.getId());
		condition.put("status", InventoryCheckStatus.FINISHED);
		condition.put("etime", new Date());
		inventoryCheckService.updateInventoryCheckStatus(condition);
	}
	
	
	
}
