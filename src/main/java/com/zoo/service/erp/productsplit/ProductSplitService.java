package com.zoo.service.erp.productsplit;

import java.math.BigDecimal;
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

import com.zoo.controller.erp.constant.JournalAccountType;
import com.zoo.controller.erp.constant.ProductSplitStatus;
import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.outbound.OutboundDetailMapper;
import com.zoo.mapper.erp.outbound.OutboundMapper;
import com.zoo.mapper.erp.productsplit.ProductSplitDetailMapper;
import com.zoo.mapper.erp.productsplit.ProductSplitMapper;
import com.zoo.mapper.erp.warehouse.GoodsAllocationMapper;
import com.zoo.mapper.erp.warehouse.StockDetailMapper;
import com.zoo.mapper.erp.warehouse.StockMapper;
import com.zoo.model.erp.JournalAccount;
import com.zoo.model.erp.outbound.Outbound;
import com.zoo.model.erp.outbound.OutboundDetail;
import com.zoo.model.erp.productsplit.ProductSplit;
import com.zoo.model.erp.productsplit.ProductSplitDetail;
import com.zoo.model.erp.warehouse.GoodsAllocation;
import com.zoo.model.erp.warehouse.Stock;
import com.zoo.model.erp.warehouse.StockDetail;
import com.zoo.model.system.user.UserInfo;
import com.zoo.service.erp.JournalAccountService;
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
	
	@Autowired
	OutboundMapper outboundMapper;
	
	@Autowired
	OutboundDetailMapper outboundDetailMapper;
	
	@Autowired
	GoodsAllocationMapper gaMapper;
	
	@Autowired
	private JournalAccountService journalAccountService;
	/*
	 * public List<ProductSplit> getProductSplitByPage(Integer page, Integer size){
	 * Integer start = null; if(page != null) { start = (page - 1) * size; } return
	 * productSplitMapper.getProductSplitByPage(start, size,
	 * LoginInterceptor.getLoginUser().getCompanyId()); }
	 * 
	 * 
	 * public Long getCount() { return
	 * productSplitMapper.getCount(LoginInterceptor.getLoginUser().getCompanyId());
	 * }
	 */
	
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
		detailMapper.deleteByProductSplitId(productSplit.getId());
		for(ProductSplitDetail detail : productSplit.getDetails()) {
			detail.setId(UUID.randomUUID().toString());
			detail.setCtime(new Date());
			detail.setTotalNumber(detail.getNumber().multiply(productSplit.getNumber()));
			detail.setProductSplitId(productSplit.getId());
			detail.setNotInNumber(detail.getNumber());
			detailMapper.addDetail(detail);
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
		productSplit.setNotOutNumber(productSplit.getNumber());
		productSplit.setStatus(ProductSplitStatus.WTJ);
		productSplitMapper.addProductSplit(productSplit);
		for(ProductSplitDetail detail : productSplit.getDetails()) {
			detail.setId(UUID.randomUUID().toString());
			detail.setCtime(new Date());
			detail.setTotalNumber(detail.getNumber().multiply(productSplit.getNumber()));
			detail.setProductSplitId(id);
			detail.setNotInNumber(detail.getTotalNumber());
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

	//添加出库单
	public void addOutbound(Outbound outbound, String goodsAllocationId, BigDecimal number) {
		// TODO Auto-generated method stub
		//获取出库单
		Outbound ob = outboundMapper.getOutboundByForeignKey(outbound.getForeignKey());
		//获取拆分单
		ProductSplit split = this.getProductSplitById(outbound.getForeignKey());
		//获取货位
		GoodsAllocation goodsAllocation = gaMapper.getGoodsAllocationById(goodsAllocationId);
		
		OutboundDetail outboundDetail = new OutboundDetail();
		if(ob != null) {
			for(OutboundDetail detail: ob.getDetails()) {
				if(detail.getGoodsAllocation().getId().equals(goodsAllocationId)) {
					detail.setNumber(detail.getNumber().add(number));
					outboundDetailMapper.update(detail);
					break;
				}else if (!detail.getGoodsAllocation().getId().equals(goodsAllocationId)) {
					outboundDetail.setId(UUID.randomUUID().toString());
					outboundDetail.setCtime(new Date());
					outboundDetail.setGoodsAllocation(goodsAllocation);
					outboundDetail.setNumber(number);
					outboundDetail.setOutboundId(ob.getId());
					outboundDetail.setProduct(split.getProduct());
					outboundDetailMapper.addDetail(outboundDetail);
					break;
				}
			}
			
		}else {
			outbound.setId(UUID.randomUUID().toString());
			outbound.setCode(split.getCode());
			outbound.setType("CF");
			outbound.setCuserId(LoginInterceptor.getLoginUser().getId());
			outbound.setCtime(new Date());
			outbound.setWarehouse(split.getWarehouse());
			outboundMapper.addOutbound(outbound);
			outboundDetail.setId(UUID.randomUUID().toString());
			outboundDetail.setCtime(new Date());
			outboundDetail.setGoodsAllocation(goodsAllocation);
			outboundDetail.setNumber(number);
			outboundDetail.setOutboundId(outbound.getId());
			outboundDetail.setProduct(split.getProduct());
			outboundDetailMapper.addDetail(outboundDetail);
		}
		/*--------------更新仓库库存开始----------------*/
		Stock stock = stockMapper.getStock(split.getProduct().getId(), split.getWarehouse().getId());
		
		if(stock != null) {
			//更新后使用数量
			BigDecimal after_usableNumber = stock.getUsableNumber().subtract(number);
			//更新后总额
			BigDecimal after_totalMoney = stock.getTotalMoney().subtract(after_usableNumber.multiply(stock.getCostPrice()));
			
			stock.setUsableNumber(after_usableNumber);
			stock.setTotalMoney(after_totalMoney);
			stockMapper.updateStock(stock);
		}else {
			throw new ZooException(ExceptionEnum.STOCK_NOT_FOUND);
		}
		/*--------------更新仓库库存结束----------------*/
		/*--------------更新货位库存开始----------------*/
		//获取货位库存
		StockDetail stockDetail = stockDetailMapper.getDetail(stock.getId(), goodsAllocationId);
		if(stockDetail != null) {
			//更新可用数量
			stockDetail.setUsableNumber(stockDetail.getUsableNumber().subtract(number));
			stockDetailMapper.updateStockDetail(stockDetail);
		}
		/*--------------更新货位库存结束----------------*/
		
		/*------------------库存变动明细开始----------------------*/
		JournalAccount account = new JournalAccount();
		account.setId(UUID.randomUUID().toString());
		account.setType(JournalAccountType.SPLIT);
		account.setOrderDetailId(split.getId());
		account.setOrderCode(split.getCode());
		account.setStock(stock);
		account.setCkNumber(stock.getUsableNumber());
		account.setCkPrice(stock.getCostPrice());
		account.setCkTotalMoney(stock.getTotalMoney());
		account.setCtime(new Date());
		account.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
		account.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		journalAccountService.addJournalAccount(account);
		/*------------------库存变动明细结束----------------------*/
	}

	

}
