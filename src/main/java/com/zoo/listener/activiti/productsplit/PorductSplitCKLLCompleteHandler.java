package com.zoo.listener.activiti.productsplit;

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
import com.zoo.controller.erp.constant.JournalAccountType;
import com.zoo.controller.erp.constant.ProductSplitStatus;
import com.zoo.filter.LoginInterceptor;
import com.zoo.model.erp.JournalAccount;
import com.zoo.model.erp.productsplit.ProductSplit;
import com.zoo.model.erp.productsplit.ProductSplitDetail;
import com.zoo.model.erp.warehouse.Stock;
import com.zoo.model.erp.warehouse.StockDetail;
import com.zoo.service.erp.JournalAccountService;
import com.zoo.service.erp.productsplit.ProductSplitService;
import com.zoo.service.erp.warehouse.StockDetailService;
import com.zoo.service.erp.warehouse.StockService;
import com.zoo.utils.ApplicationUtil;
/**
 * 仓库领料完成
 * @author aa
 *
 */
@Component("porductSplitCKLLCompleteHandler")
public class PorductSplitCKLLCompleteHandler implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6117303043818737751L;

	private ProcessEngine processEngine;
	private StockService stockService;
	private StockDetailService stockDetailService;
	private JournalAccountService journalAccountService;
	private ProductSplitService productSplitService;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		processEngine = (ProcessEngine) ApplicationUtil.getBean("processEngine");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance result = runtimeService.createProcessInstanceQuery().processInstanceId(delegateTask.getProcessInstanceId()).singleResult();
		String key = result.getBusinessKey();
		stockService = (StockService)ApplicationUtil.getBean("stockService");
		stockDetailService = (StockDetailService)ApplicationUtil.getBean("stockDetailService");
		productSplitService = (ProductSplitService) ApplicationUtil.getBean("productSplitService");
		journalAccountService = (JournalAccountService)ApplicationUtil.getBean("journalAccountService");
		
		//获取拆分单
		ProductSplit split = productSplitService.getProductSplitById(key);
		//获取仓库id
		String warehouseId = split.getWarehouse().getId();
		//获取产品id
		String productId = split.getProduct().getId();
		//获取库存
		Stock stock = stockService.getStock(productId, warehouseId);
		
		//获取货位库存信息
		StockDetail stockDetail = stockDetailService.getStockDetail(stock.getId(), split.getGoodsAllocation().getId());
		
		//更新货位库存数量
		stockDetail.setUsableNumber(stockDetail.getUsableNumber().subtract(split.getNumber()));
		stockDetailService.updateStockDetail(stockDetail);
		
		/*
		 * 更新库存信息开始
		 */
		//变化后可用数量
		BigDecimal after_usableNumber = stock.getUsableNumber().subtract(split.getNumber());
		//变化后库存总额
		BigDecimal after_totalMoney = stock.getTotalMoney().subtract(split.getNumber().multiply(stock.getCostPrice()));
		
		//更新库存
		stock.setUsableNumber(after_usableNumber);
		stock.setTotalMoney(after_totalMoney);
		stockService.updateStock(stock);
		/*
		 * 更新库存信息结束
		 */
		//添加库存变化明细
		JournalAccount journalAccount = new JournalAccount();
		journalAccount.setId(UUID.randomUUID().toString());
		journalAccount.setType(JournalAccountType.SPLIT);
		journalAccount.setOrderDetailId(split.getId());
		journalAccount.setOrderCode(split.getCode());
		journalAccount.setStock(stock);
		journalAccount.setCkNumber(split.getNumber());
		journalAccount.setCkPrice(stock.getCostPrice());
		journalAccount.setCkTotalMoney(split.getNumber());
		journalAccount.setCtime(new Date());
		journalAccount.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
		journalAccount.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		journalAccountService.addJournalAccount(journalAccount);
		//更新订单状态
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("id", split.getId());
		condition.put("status", ProductSplitStatus.CF);
		productSplitService.updateProductSplitStatus(condition);
		
	}
}
