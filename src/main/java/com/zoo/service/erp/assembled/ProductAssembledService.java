package com.zoo.service.erp.assembled;

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
import com.zoo.controller.erp.constant.AssembledStatus;
import com.zoo.controller.erp.constant.JournalAccountType;
import com.zoo.controller.erp.constant.ProductAssembledStatus;
import com.zoo.controller.erp.constant.SellStatus;
import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.assembled.ProductAssembledMapper;
import com.zoo.mapper.erp.assembled.ProductAssembledMaterialMapper;
import com.zoo.mapper.erp.inbound.InboundDetailMapper;
import com.zoo.mapper.erp.inbound.InboundMapper;
import com.zoo.mapper.erp.outbound.OutboundMapper;
import com.zoo.mapper.erp.warehouse.GoodsAllocationMapper;
import com.zoo.mapper.erp.warehouse.StockDetailMapper;
import com.zoo.mapper.erp.warehouse.StockMapper;
import com.zoo.model.erp.JournalAccount;
import com.zoo.model.erp.assembled.ProductAssembled;
import com.zoo.model.erp.assembled.ProductAssembledMaterial;
import com.zoo.model.erp.inbound.Inbound;
import com.zoo.model.erp.inbound.InboundDetail;
import com.zoo.model.erp.outbound.Outbound;
import com.zoo.model.erp.outbound.OutboundDetail;
import com.zoo.model.erp.warehouse.Stock;
import com.zoo.model.erp.warehouse.StockDetail;
import com.zoo.model.system.user.UserInfo;
import com.zoo.service.erp.JournalAccountService;
import com.zoo.service.erp.inbound.InboundDetailService;
import com.zoo.service.erp.inbound.InboundService;
import com.zoo.service.erp.warehouse.StockDetailService;
import com.zoo.service.erp.warehouse.StockService;
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
	
	@Autowired
	StockMapper stockMapper;
	@Autowired
	StockDetailMapper stockDetailMapper;
	
	@Autowired
	private InboundMapper inboundMapper;
	@Autowired
	private InboundDetailMapper inboundDetailMapper;
	@Autowired
	private JournalAccountService journalAccountService;
	@Autowired
	GoodsAllocationMapper gaMapper;
	@Autowired
	private OutboundMapper outboundMapper;
	
	@Autowired
	private InboundDetailService inboundDetailService;
	
	@Autowired
	private InboundService inboundService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private StockService stockService;
	
	@Autowired 
	private StockDetailService stockDetailService;
	
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
		productAssembled.setNotInNumber(productAssembled.getNumber());
		productAssembled.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		productAssembled.setStatus(AssembledStatus.WTJ);
		paMapper.addProductAssembled(productAssembled);
		
		for(ProductAssembledMaterial material:productAssembled.getMaterials()) {
			material.setId(UUID.randomUUID().toString());
			material.setProductAssembledId(id);
			material.setNotOutNumber(material.getNeedNumber());
			pamMapper.addMaterial(material);
		}
	}
	public void updateProductAssembled(ProductAssembled productAssembled) {

		paMapper.updateProductAssembled(productAssembled);
		pamMapper.deleteMaterialByAllembledId(productAssembled.getId());
		for(ProductAssembledMaterial material:productAssembled.getMaterials()) {
			material.setId(UUID.randomUUID().toString());
			material.setProductAssembledId(productAssembled.getId());
			material.setNotOutNumber(material.getNeedNumber());
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
		variables.put("warehouseId", pa.getWarehouse().getId());
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
	
	//流程取回
	public void takeBack(String id) {
		// TODO Auto-generated method stub
		ProductAssembled pa = this.getProductAssembledById(id);
		Task task = taskService.createTaskQuery().processInstanceId(pa.getProcessInstanceId()).active().singleResult();
		if("assembledckzg".equals(task.getTaskDefinitionKey())) {
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
	
	public int updateProductAssembledStatus(Map<String, Object> condition) {
		return paMapper.updateProductAssembledStatus(condition);
	}
	
	/**
	 * 更新未入库数量
	 * @param notInNumber
	 * @param id
	 */
	public void updateNotInNumber(BigDecimal notInNumber, String id) {
		paMapper.updateNotInNumber(notInNumber, id);
	}
	
	/**
	 * 添加入库单
	 * @param inbound
	 */
	public void addInbound(Inbound inbound) {
		//获取组装单
		ProductAssembled assembled = paMapper.getProductAssembledById(inbound.getForeignKey());
		/*-----------添加入库单---------------*/
		inbound.setId(UUID.randomUUID().toString());
		inbound.setCode(assembled.getCode());
		inbound.setCtime(new Date());
		inbound.setCuserId(LoginInterceptor.getLoginUser().getId());
		inbound.setWarehouse(assembled.getWarehouse());
		inbound.setType("ZZ");
		inboundMapper.addInbound(inbound);
		/*-----------添加入库单结束---------------*/
		for(InboundDetail detail: inbound.getDetails()) {
			detail.setId(UUID.randomUUID().toString());
			detail.setCtime(new Date());
			detail.setInboundId(inbound.getId());
			detail.setProduct(assembled.getProduct());
			detail.setFinished(false);
			Stock stock = stockService.getStock(detail.getProduct().getId(), inbound.getWarehouse().getId());
			if(stock!=null) {
				detail.setPrice(stock.getCostPrice());
				detail.setTotalMoney(stock.getCostPrice().multiply(detail.getNumber()));
			}
			//添加入库详情单
			inboundDetailMapper.addDetail(detail);
			
			if(detail.getProduct().getId().equals(assembled.getProduct().getId())) {
				BigDecimal notInNumber = assembled.getNotInNumber().subtract(detail.getNumber());
				assembled.setNotInNumber(notInNumber);
				paMapper.updateNotInNumber(notInNumber, assembled.getId());
			}
		}
	}
	
	/**
	 * 入库单删除
	 */
	public BigDecimal deleteIn(String assembledId, String inboundDetailId, String type) {
		//获取入库单
		ProductAssembled assembled = this.getProductAssembledById(assembledId);
		BigDecimal notInNumber = assembled.getNotInNumber();//未入库数量
		InboundDetail inboundDetail = inboundDetailService.getDetailById(inboundDetailId);//获取入库详情单
		Inbound inbound = inboundService.getInboundById(inboundDetail.getInboundId());
		if("only".equals(type)) {//如果只删除一条
			
			this.deleteInDetail(inbound, inboundDetail);	
			//入库详情单删除
			inboundDetailService.deleteDetailById(inboundDetailId);
			boolean hasDetails = inboundService.checkHasDetails(inbound.getId());
			if(!hasDetails) {
				inboundService.deleteById(inbound.getId());
			}
			notInNumber = notInNumber.add(inboundDetail.getNumber());
		}else {
			for(InboundDetail detail: inbound.getDetails()) {
				this.deleteInDetail(inbound, detail);
				notInNumber = notInNumber.add(detail.getNumber());
			}
			inboundService.deleteById(inbound.getId());
		}
		this.updateNotInNumber(notInNumber, assembledId);
		return notInNumber;
	}
	
	private void deleteInDetail(Inbound inbound, InboundDetail inboundDetail) {
		if(inboundDetail.getFinished()) {
			//获取库存
			Stock stock = stockService.getStock(inboundDetail.getProduct().getId(), inbound.getWarehouse().getId());
			//获取库存详情
			StockDetail stockDetail = stockDetailService.getStockDetail(stock.getId(), inboundDetail.getGoodsAllocation().getId());
			if(stockDetail.getUsableNumber().subtract(inboundDetail.getNumber()).compareTo(BigDecimal.ZERO) == -1) {
				throw new ZooException(inboundDetail.getProduct().getName() + ExceptionEnum.STOCK_NOT_ENOUGH);
			}else {
				stockDetail.setUsableNumber(stockDetail.getUsableNumber().subtract(inboundDetail.getNumber()));
				stockDetailService.updateStockDetail(stockDetail);
				
				//更新库存
				//变更后的可用数量
				BigDecimal after_usableNumber = stock.getUsableNumber().subtract(inboundDetail.getNumber());
				//变更后的总额
				BigDecimal after_totalMoney = stock.getTotalMoney().subtract(inboundDetail.getTotalMoney());
				//更新后单价
				BigDecimal after_costPrice;
				
				if(after_usableNumber.compareTo(BigDecimal.ZERO)==0) {
					after_totalMoney = new BigDecimal("0");
					after_costPrice = stock.getCostPrice();
				}else {
					after_costPrice = after_totalMoney.divide(after_usableNumber,4,BigDecimal.ROUND_HALF_UP);
				}
				
				stock.setUsableNumber(after_usableNumber);
				stock.setTotalMoney(after_totalMoney);
				stock.setCostPrice(after_costPrice);
				stockService.updateStock(stock);
				
				//添加库存明细单
				JournalAccount account = new JournalAccount();
				account.setId(UUID.randomUUID().toString());
				account.setType(JournalAccountType.ASSEMBLEDRKDELETE);
				account.setOrderDetailId("");
				account.setOrderCode(inbound.getCode());
				account.setStock(stock);
				account.setCkNumber(inboundDetail.getNumber());
				account.setCkPrice(inboundDetail.getPrice());
				account.setCkTotalMoney(inboundDetail.getTotalMoney());
				account.setCtime(new Date());
				account.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
				account.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
				journalAccountService.addJournalAccount(account);
			}
		}
		
	}
	
	/**
	 * 作废
	 * @param id
	 */
	public void destroy(String id) {
		//获取组装单
		ProductAssembled assembled = this.getProductAssembledById(id);
		String status = assembled.getStatus();
		//更新订单状态
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("id", id);
		condition.put("status", ProductAssembledStatus.DESTROY);
		condition.put("etime", new Date());
		this.updateProductAssembledStatus(condition);
		//更新订单状态结束
		
		//删除流程
		if(StringUtil.isNotEmpty(assembled.getProcessInstanceId()) && !ProductAssembledStatus.WTJ.equals(status)) {
			if(ProductAssembledStatus.FINISHED.equals(status)) {
				//删除历史流程
				historyService.deleteHistoricProcessInstance(assembled.getProcessInstanceId());
			}else {
				RuntimeService runtimeService = processEngine.getRuntimeService();
				runtimeService.deleteProcessInstance(assembled.getProcessInstanceId(), "待定");
			}
		}
		paMapper.updateProcessInstanceId(id, null);
		
		//还原出库
		List<Outbound> outbounds = outboundMapper.getOutboundByForeignKey(id);
		for(Outbound outbound: outbounds) {
			for(OutboundDetail outboundDetail: outbound.getDetails()) {
				Stock stock = stockMapper.getStock(outboundDetail.getProduct().getId(),outbound.getWarehouse().getId());
				StockDetail stockDetail = stockDetailMapper.getDetail(stock.getId(), outboundDetail.getGoodsAllocation().getId());
				stockDetail.setUsableNumber(stockDetail.getUsableNumber().add(outboundDetail.getNumber()));
				stockDetailMapper.updateStockDetail(stockDetail);
				
				BigDecimal after_usableNumber = stock.getUsableNumber().add(outboundDetail.getNumber());
				BigDecimal after_totalMoney = stock.getTotalMoney().add(outboundDetail.getTotalMoney());
				BigDecimal after_costPrice = after_totalMoney.divide(after_usableNumber, 4, BigDecimal.ROUND_HALF_UP);
				stock.setCostPrice(after_costPrice);
				stock.setTotalMoney(after_totalMoney);
				stock.setUsableNumber(after_usableNumber);
				stockMapper.updateStock(stock);
				
				JournalAccount account = new JournalAccount();
				account.setId(UUID.randomUUID().toString());
				account.setType(JournalAccountType.ASSEMBLEDDESTROY);
				account.setOrderDetailId("");
				account.setOrderCode(assembled.getCode());
				account.setStock(stock);
				account.setRkNumber(outboundDetail.getNumber());
				account.setRkPrice(outboundDetail.getPrice());
				account.setRkTotalMoney(outboundDetail.getTotalMoney());
				account.setCtime(new Date());
				account.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
				account.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
				journalAccountService.addJournalAccount(account);
			}
		}
		//还原入库
		List<Inbound> inbounds = inboundService.getInboundByForeignKey(id);
		for(Inbound inbound: inbounds) {
			for(InboundDetail inboundDetail: inbound.getDetails()) {
				if(inboundDetail.getFinished()) {
					Stock stock = stockService.getStock(inboundDetail.getProduct().getId(), inbound.getWarehouse().getId());
					
					if(stock.getUsableNumber().subtract(inboundDetail.getNumber()).compareTo(BigDecimal.ZERO)==-1) {
						throw new ZooException(inboundDetail.getProduct().getName()+"库存不足，不能作废");
					}else {
						StockDetail stockDetail = stockDetailService.getStockDetail(stock.getId(), inboundDetail.getGoodsAllocation().getId());
						if(stockDetail.getUsableNumber().subtract(inboundDetail.getNumber()).compareTo(BigDecimal.ZERO)==-1) {
							throw new ZooException(inboundDetail.getProduct().getName()+"在货位"+inboundDetail.getGoodsAllocation().getName()+"上库存不足");
						}else {
							stockDetail.setUsableNumber(stockDetail.getUsableNumber().subtract(inboundDetail.getNumber()));
							stockDetailService.updateStockDetail(stockDetail);
							
							BigDecimal after_usableNumber = stock.getUsableNumber().subtract(inboundDetail.getNumber());
							BigDecimal after_totalMoney = stock.getTotalMoney().subtract(inboundDetail.getTotalMoney());
							BigDecimal after_costPrice;
							if(after_usableNumber.compareTo(BigDecimal.ZERO)==0) {
								after_totalMoney = new BigDecimal("0");
								after_costPrice = stock.getCostPrice();
							}else {
								after_costPrice = after_totalMoney.divide(after_usableNumber,4,BigDecimal.ROUND_HALF_UP);
							}
							
							stock.setCostPrice(after_costPrice);
							stock.setTotalMoney(after_totalMoney);
							stock.setUsableNumber(after_usableNumber);
							stockService.updateStock(stock);
							JournalAccount journalAccount = new JournalAccount();
							journalAccount.setId(UUID.randomUUID().toString());
							journalAccount.setType(JournalAccountType.ASSEMBLEDDESTROY);
							journalAccount.setOrderDetailId("");
							journalAccount.setOrderCode(assembled.getCode());
							journalAccount.setStock(stock);
							journalAccount.setCkNumber(inboundDetail.getNumber());
							journalAccount.setCkPrice(inboundDetail.getPrice());
							journalAccount.setCkTotalMoney(inboundDetail.getTotalMoney());
							journalAccount.setCtime(new Date());
							journalAccount.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
							journalAccount.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
							journalAccountService.addJournalAccount(journalAccount);
						}
						
					}
				}
			}
		}
	}
	
	/**
	 * 确认入库
	 * @param id
	 * @throws Exception 
	 */
	public void inboundOperation(String id) {
		// TODO Auto-generated method stub
		
		InboundDetail detail = inboundDetailService.getDetailById(id);//获取入库详情单
		Inbound inbound = inboundService.getInboundById(detail.getInboundId());
		if(detail.getFinished()) {
			throw new ZooException("不能重复入库");
		}else {
			Stock stock = stockService.getStock(detail.getProduct().getId(), inbound.getWarehouse().getId());//库存
			if(stock!=null) {
				StockDetail stockDetail = stockDetailService.getStockDetail(stock.getId(), detail.getGoodsAllocation().getId());//库存详情
				if(stockDetail!=null) {
					stockDetail.setUsableNumber(stockDetail.getUsableNumber().add(detail.getNumber()));
					stockDetailService.updateStockDetail(stockDetail);//更新库存详情	
				}else {
					stockDetail = new StockDetail();
					stockDetail.setId(UUID.randomUUID().toString());
					stockDetail.setUsableNumber(detail.getNumber());
					stockDetail.setStockId(stock.getId());
					stockDetailService.addStockDetail(stockDetail);
				}
				BigDecimal after_usableNumber = stock.getUsableNumber().add(detail.getNumber());
				BigDecimal after_totalMoney = stock.getTotalMoney().add(detail.getTotalMoney());
				BigDecimal after_costPrice = after_totalMoney.divide(after_usableNumber);
				stock.setCostPrice(after_costPrice);
				stock.setTotalMoney(after_totalMoney);
				stock.setUsableNumber(after_usableNumber);
				stockMapper.updateStock(stock);
			}else {
				stock = new Stock();
				stock.setId(UUID.randomUUID().toString());
				stock.setWarehouse(inbound.getWarehouse());
				stock.setProduct(detail.getProduct());
				stock.setCostPrice(detail.getPrice());
				stock.setUsableNumber(detail.getNumber());
				stock.setTotalMoney(detail.getTotalMoney());
				stockService.addStock(stock);
				
				StockDetail stockDetail = new StockDetail();
				stockDetail.setId(UUID.randomUUID().toString());
				stockDetail.setStockId(stock.getId());
				stockDetail.setGoodsAllocation(detail.getGoodsAllocation());
				stockDetail.setUsableNumber(detail.getNumber());
				stockDetailService.addStockDetail(stockDetail);
			}
			
			//添加库存变动明细表
			JournalAccount account = new JournalAccount();
			account.setId(UUID.randomUUID().toString());
			account.setType(JournalAccountType.ASSEMBLEDRK);
			account.setOrderCode(inbound.getCode());
			account.setOrderDetailId("");
			account.setStock(stock);
			account.setRkNumber(detail.getNumber());
			account.setRkPrice(detail.getPrice());
			account.setRkTotalMoney(detail.getTotalMoney());
			account.setCtime(new Date());
			account.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
			account.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
			journalAccountService.addJournalAccount(account);
			inboundDetailService.updateFinished(id, true);
		}
	
	}
}
