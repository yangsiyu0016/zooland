package com.zoo.service.erp.productsplit;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.util.StringUtil;
import com.zoo.controller.erp.constant.JournalAccountType;
import com.zoo.controller.erp.constant.OpeningInventoryStatus;
import com.zoo.controller.erp.constant.ProductSplitStatus;
import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.productsplit.ProductSplitMapper;
import com.zoo.mapper.erp.warehouse.GoodsAllocationMapper;
import com.zoo.model.erp.JournalAccount;
import com.zoo.model.erp.inbound.Inbound;
import com.zoo.model.erp.inbound.InboundDetail;
import com.zoo.model.erp.outbound.Outbound;
import com.zoo.model.erp.outbound.OutboundDetail;
import com.zoo.model.erp.productsplit.ProductSplit;
import com.zoo.model.erp.productsplit.ProductSplitDetail;
import com.zoo.model.erp.warehouse.Stock;
import com.zoo.model.erp.warehouse.StockDetail;
import com.zoo.model.system.user.UserInfo;
import com.zoo.service.erp.JournalAccountService;
import com.zoo.service.erp.inbound.InboundDetailService;
import com.zoo.service.erp.inbound.InboundService;
import com.zoo.service.erp.outbound.OutboundDetailService;
import com.zoo.service.erp.outbound.OutboundService;
import com.zoo.service.erp.warehouse.StockDetailService;
import com.zoo.service.erp.warehouse.StockService;
import com.zoo.service.system.parameter.SystemParameterService;
import com.zoo.utils.CodeGenerator;

@Service
@Transactional
public class ProductSplitService {

	@Autowired
	private ProductSplitMapper productSplitMapper;
	
	@Autowired
	private ProductSplitDetailService detailService;
	
	@Autowired
	ProcessEngine processEngine;
	
	@Autowired
	IdentityService identityService;
	@Autowired
	StockService stockService;
	@Autowired
	StockDetailService stockDetailService;
	@Autowired
	SystemParameterService systemParameterService;
	
	@Autowired
	OutboundService outboundService;
	
	@Autowired
	OutboundDetailService outboundDetailService;
	@Autowired
	InboundService inboundService;
	@Autowired
	InboundDetailService inboundDetailService;
	@Autowired
	GoodsAllocationMapper gaMapper;
	
	@Autowired
	private JournalAccountService journalAccountService;
	@Autowired
	HistoryService historyService;
	@Autowired
	TaskService taskService;
	/**
	 * 分页查询
	 * @param page
	 * @param size
	 * @return
	 */
	public List<ProductSplit> getProductSplitByPage(Integer page, Integer size, String keywords, String code,
			String productCode, String productName, String status, String warehouseId, String start_splitTime,
			String end_splitTime, String start_ctime, String end_ctime, String sort, String order) {
		// TODO Auto-generated method stub
		Integer start = null;
		if(page != null) {
			start = (page - 1) * size;
		}
		
		return productSplitMapper.getProductSplitByPage(start, size, keywords, code, productCode, productName, status,
				warehouseId, start_splitTime, end_splitTime, start_ctime, end_ctime, LoginInterceptor.getLoginUser().getCompanyId(), sort, order);
	}

	/**
	 * 获取总条数
	 * @return
	 */
	public Long getCount(String keywords, String code, String productCode, String productName, String status,
			String warehouseId, String start_splitTime, String end_splitTime, String start_ctime, String end_ctime,
			String sort, String order) {
		// TODO Auto-generated method stub
		return productSplitMapper.getCount(keywords, code, productCode, productName, status, warehouseId, start_splitTime, end_splitTime,
				start_ctime, end_ctime, LoginInterceptor.getLoginUser().getCompanyId());
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
		detailService.deleteByProductSplitId(productSplit.getId());
		for(ProductSplitDetail detail : productSplit.getDetails()) {
			detail.setId(UUID.randomUUID().toString());
			detail.setCtime(new Date());
			detail.setTotalNumber(detail.getNumber().multiply(productSplit.getNumber()));
			detail.setProductSplitId(productSplit.getId());
			detail.setNotInNumber(detail.getNumber());
			detailService.addDetail(detail);
		}
	}
	
	public void updateNotOutNumberById(BigDecimal notOutNumber, String id) {
		productSplitMapper.updateNotOutNumberById(notOutNumber, id);
	}
	
	public void deleteDetailById(String ids) {
		// TODO Auto-generated method stub
		String[] split = ids.split(",");
		productSplitMapper.deleteProductSplitByIds(split);
		for(String id: split) {
			ProductSplit productSplit = this.getProductSplitById(id);
			if(StringUtil.isNotEmpty(productSplit.getProcessInstanceId())) {
				throw new ZooException("流程已启动,不能删除");
			}
			detailService.deleteByProductSplitId(id);
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
		productSplit.setNotOutNumber(productSplit.getNumber());
		productSplit.setStatus(ProductSplitStatus.WTJ);
		productSplitMapper.addProductSplit(productSplit);
		for(ProductSplitDetail detail : productSplit.getDetails()) {
			detail.setId(UUID.randomUUID().toString());
			detail.setCtime(new Date());
			detail.setTotalNumber(detail.getNumber().multiply(productSplit.getNumber()));
			detail.setProductSplitId(id);
			detail.setNotInNumber(detail.getTotalNumber());
			detailService.addDetail(detail);
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
		if(StringUtil.isNotEmpty(split.getProcessInstanceId())) {
			throw new ZooException("流程已启动");
		}else {
			UserInfo info = LoginInterceptor.getLoginUser();
			Map<String, Object> variables = new HashMap<String, Object>();
			
			// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
			identityService.setAuthenticatedUserId(info.getId());
			
			String businessKey = id;
			
			variables.put("CODE", split.getCode());
			variables.put("warehouseId", split.getWarehouse().getId());
			//启动流程
			RuntimeService runtimeService = processEngine.getRuntimeService();
			ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId("productSplit", businessKey, variables,info.getCompanyId());
			//获取流程id
			String processInstanceId = processInstance.getId();
			//更新到自定义拆分表中
			productSplitMapper.updateProcessInstanceId(id, processInstanceId);
			
			Map<String,Object> condition = new HashMap<String, Object>();
			condition.put("id", id);
			condition.put("status", ProductSplitStatus.CKZG);
			productSplitMapper.updateProductSplitStatus(condition);
		}
		
	}
	/**
	 * 作废
	 * @param id
	 */
	public void destroy(String id) {
		ProductSplit split = this.getProductSplitById(id);
		String status = split.getStatus();
		
		
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("id", id);
		condition.put("status", ProductSplitStatus.DESTROY);
		condition.put("etime", new Date());
		this.updateProductSplitStatus(condition);
		
		//删除流程
		if(StringUtil.isNotEmpty(split.getProcessInstanceId())&&!status.equals(ProductSplitStatus.WTJ)) {
			if(!status.equals(ProductSplitStatus.FINISHED)) {
				RuntimeService runtimeService = processEngine.getRuntimeService();
				runtimeService.deleteProcessInstance(split.getProcessInstanceId(), "待定");
				
			}else {
				//删除历史流程
				historyService.deleteHistoricProcessInstance(split.getProcessInstanceId());
			}	
		}
		productSplitMapper.updateProcessInstanceId(id, null);
		//还原出库
		List<Outbound> outbounds = outboundService.getOutboundByForeignKey(id);
		for(Outbound outbound:outbounds) {
			for(OutboundDetail outboundDetail:outbound.getDetails()) {
				Stock stock = stockService.getStock(outboundDetail.getProduct().getId(), outbound.getWarehouse().getId());
				StockDetail stockDetail = stockDetailService.getStockDetail(stock.getId(), outboundDetail.getGoodsAllocation().getId());
				
				stockDetail.setUsableNumber(stockDetail.getUsableNumber().add(outboundDetail.getNumber()));
				stockDetailService.updateStockDetail(stockDetail);
				
				BigDecimal after_usableNumber = stock.getUsableNumber().add(outboundDetail.getNumber());
				BigDecimal after_totalMoney = stock.getTotalMoney().add(outboundDetail.getTotalMoney());
				BigDecimal after_costPrice = after_totalMoney.divide(after_usableNumber,4,BigDecimal.ROUND_HALF_UP);
				stock.setCostPrice(after_costPrice);
				stock.setTotalMoney(after_totalMoney);
				stock.setUsableNumber(after_usableNumber);
				stockService.updateStock(stock);
				
				JournalAccount journalAccount = new JournalAccount();
				journalAccount.setId(UUID.randomUUID().toString());
				journalAccount.setType(JournalAccountType.CFDESTROY);
				journalAccount.setOrderDetailId("");
				journalAccount.setOrderCode(split.getCode());
				journalAccount.setStock(stock);
				journalAccount.setRkNumber(outboundDetail.getNumber());
				journalAccount.setRkPrice(outboundDetail.getPrice());
				journalAccount.setRkTotalMoney(outboundDetail.getTotalMoney());
				journalAccount.setCtime(new Date());
				journalAccount.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
				journalAccount.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
				journalAccountService.addJournalAccount(journalAccount);
			}
		}
	}
	/**
	 * 添加入库单
	 */
	public void addInbound(Inbound inbound) {
		ProductSplit split = this.getProductSplitById(inbound.getForeignKey());
		inbound.setId(UUID.randomUUID().toString());
		inbound.setCode(split.getCode());
		inbound.setCtime(new Date());
		inbound.setCuserId(LoginInterceptor.getLoginUser().getId());
		inbound.setWarehouse(split.getWarehouse());
		inbound.setType("CF");
		inboundService.addInbound(inbound);
		
		for(InboundDetail detail:inbound.getDetails()) {
			detail.setId(UUID.randomUUID().toString());
			detail.setCtime(new Date());
			detail.setInboundId(inbound.getId());
			detail.setFinished(false);
			
			Stock stock  = stockService.getStock(detail.getProduct().getId(), inbound.getWarehouse().getId());
			if(stock!=null) {
				detail.setPrice(stock.getCostPrice());
				detail.setTotalMoney(stock.getCostPrice().multiply(detail.getNumber()));
			}
			
			inboundDetailService.addDetail(detail);
			
			for(ProductSplitDetail splitDetail:split.getDetails() ) {
				if(detail.getProduct().getId().equals(splitDetail.getProduct().getId())) {
					detailService.updateNotInNumberById(splitDetail.getNotInNumber().subtract(detail.getNumber()), splitDetail.getId());
					break;
				}
			}
		}
	}
	/**
	 * 添加入库单 减库存 加出库明细
	 * @param outbound
	 */
	public void addOutbound(Outbound outbound) {
		ProductSplit split = this.getProductSplitById(outbound.getForeignKey());
		outbound.setId(UUID.randomUUID().toString());
		outbound.setCode(split.getCode());
		outbound.setCtime(new Date());
		outbound.setCuserId(LoginInterceptor.getLoginUser().getId());
		outbound.setWarehouse(split.getWarehouse());
		outbound.setType("CF");
		outboundService.addOutBound(outbound);
		
		for(OutboundDetail detail:outbound.getDetails()) {
			detail.setId(UUID.randomUUID().toString());
			detail.setCtime(new Date());
			detail.setOutboundId(outbound.getId());
			detail.setProduct(split.getProduct());
			Stock stock = stockService.getStock(split.getProduct().getId(), split.getWarehouse().getId());
			
			if(stock!=null) {
				if(stock.getUsableNumber().subtract(detail.getNumber()).compareTo(BigDecimal.ZERO)==-1) {
					throw new ZooException(split.getProduct().getName()+"库存不足");
				}else {
					StockDetail stockDetail = stockDetailService.getStockDetail(stock.getId(), detail.getGoodsAllocation().getId());
					if(stockDetail!=null) {
						if(stockDetail.getUsableNumber().subtract(detail.getNumber()).compareTo(BigDecimal.ZERO)==-1) {
							throw new ZooException(ExceptionEnum.STOCK_DETAIL_NO_ENOUGH);
						}else {
							stockDetail.setUsableNumber(stockDetail.getUsableNumber().subtract(detail.getNumber()));
							stockDetailService.updateStockDetail(stockDetail);
							
							BigDecimal after_usableNumber = stock.getUsableNumber().subtract(detail.getNumber());
							BigDecimal after_totalMoney,after_costPrice;
							stock.setUsableNumber(after_usableNumber);
							
							BigDecimal ckTotalMoney,ckPrice;
							if(after_usableNumber.compareTo(BigDecimal.ZERO)==0) {
								ckTotalMoney = stock.getTotalMoney();
								ckPrice = stock.getCostPrice();
								after_totalMoney = new BigDecimal("0");
								after_costPrice = stock.getCostPrice();
							}else {
								ckPrice = stock.getCostPrice();
								ckTotalMoney = ckPrice.multiply(detail.getNumber());
								after_totalMoney = stock.getTotalMoney().subtract(ckTotalMoney);
								after_costPrice = after_totalMoney.divide(after_usableNumber,4,BigDecimal.ROUND_HALF_UP);
							}
							stock.setCostPrice(after_costPrice);
							stock.setTotalMoney(after_totalMoney);
							stockService.updateStock(stock);
							
							detail.setPrice(ckPrice);
							detail.setTotalMoney(ckTotalMoney);
							outboundDetailService.addDetail(detail);
							
							
							JournalAccount journalAccount = new JournalAccount();
							journalAccount.setId(UUID.randomUUID().toString());
							journalAccount.setType(JournalAccountType.SPLITCK);
							journalAccount.setOrderDetailId(detail.getId());
							journalAccount.setOrderCode(split.getCode());
							journalAccount.setStock(stock);
							journalAccount.setCkNumber(detail.getNumber());
							journalAccount.setCkPrice(ckPrice);
							journalAccount.setCkTotalMoney(ckTotalMoney);
							journalAccount.setCtime(new Date());
							journalAccount.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
							journalAccount.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
							journalAccountService.addJournalAccount(journalAccount);
							
							BigDecimal notOutNumber = split.getNotOutNumber().subtract(detail.getNumber());
							
							this.updateNotOutNumberById(notOutNumber, split.getId());
							
							
						}
					}else {
						throw new ZooException(detail.getGoodsAllocation().getName()+"货位库存不存在");
					}
				}
			}else {
				throw new ZooException(split.getProduct().getName()+"库存不存在");
			}

		}
	}
	public BigDecimal deleteOut(String splitId,String outboundDetailId,String type) {
		ProductSplit split = this.getProductSplitById(splitId);
		BigDecimal notOutNumber = split.getNotOutNumber();
		OutboundDetail outboundDetail = outboundDetailService.getDetailById(outboundDetailId);
		Outbound outbound = outboundService.getOutboundById(outboundDetail.getOutboundId());
		if(type.equals("only")) {
			
			this.deleteOutDetail(outboundDetail, outbound);
			
			outboundDetailService.deleteDetailById(outboundDetailId);
			
			boolean hasDetails = outboundService.checkHasDetails(outbound.getId());
			if(!hasDetails) {
				outboundService.deleteById(outbound.getId());
			}
			notOutNumber  = notOutNumber.add(outboundDetail.getNumber());
			//this.updateNotOutNumberById(notOutNumber, splitId);
		}else {
			for(OutboundDetail detail:outbound.getDetails()) {
				this.deleteOutDetail(detail, outbound);
				notOutNumber= notOutNumber.add(detail.getNumber());
			}
			outboundService.deleteById(outbound.getId());
			
		}
		this.updateNotOutNumberById(notOutNumber, splitId);
		return notOutNumber;
		
	}
	private void deleteOutDetail(OutboundDetail outboundDetail,Outbound outbound) {
		Stock stock = stockService.getStock(outboundDetail.getProduct().getId(), outbound.getWarehouse().getId());
		
		StockDetail stockDetail = stockDetailService.getStockDetail(stock.getId(), outboundDetail.getGoodsAllocation().getId());
		stockDetail.setUsableNumber(stockDetail.getUsableNumber().add(outboundDetail.getNumber()));
		stockDetailService.updateStockDetail(stockDetail);
		
		BigDecimal after_usableNumber = stock.getUsableNumber().add(outboundDetail.getNumber());
		BigDecimal after_totalMoney = stock.getTotalMoney().add(outboundDetail.getTotalMoney());
		BigDecimal after_costPrice = after_totalMoney.divide(after_usableNumber,4,BigDecimal.ROUND_HALF_UP);
		stock.setUsableNumber(after_usableNumber);
		stock.setTotalMoney(after_totalMoney);
		stock.setCostPrice(after_costPrice);
		stockService.updateStock(stock);
		
		JournalAccount journalAccount = new JournalAccount();
		journalAccount.setId(UUID.randomUUID().toString());
		journalAccount.setType(JournalAccountType.SPLITCKDELETE);
		journalAccount.setOrderDetailId("");
		journalAccount.setOrderCode(outbound.getCode());
		journalAccount.setStock(stock);
		journalAccount.setRkNumber(outboundDetail.getNumber());
		journalAccount.setRkPrice(outboundDetail.getPrice());
		journalAccount.setRkTotalMoney(outboundDetail.getTotalMoney());
		journalAccount.setCtime(new Date());
		journalAccount.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
		journalAccount.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		journalAccountService.addJournalAccount(journalAccount);
	}
	public void reset(String id) {
		// TODO Auto-generated method stub
		ProductSplit split = this.getProductSplitById(id);
		Task task = taskService.createTaskQuery().processInstanceId(split.getProcessInstanceId()).active().singleResult();
		if(task==null) throw new ZooException("任务不存在");
		if(task.getTaskDefinitionKey().equals("productsplitckzg")) {
			if(StringUtil.isEmpty(task.getAssignee())) {
				Map<String,Object> condition = new HashMap<String, Object>();
				condition.put("id", id);
				condition.put("status", OpeningInventoryStatus.WTJ);
				productSplitMapper.updateProductSplitStatus(condition);
				//删除流程
				RuntimeService runtimeService = processEngine.getRuntimeService();
				runtimeService.deleteProcessInstance(split.getProcessInstanceId(), "待定");
				productSplitMapper.updateProcessInstanceId(id, null);
			}else {
				throw new ZooException("审批人已签收不能取回");
			}
		}else {
			throw new ZooException("当前节点不能取回");
		}
	}

	

}
