package com.zoo.service.erp.assembled;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

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
import com.zoo.mapper.erp.outbound.OutboundDetailMapper;
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
import com.zoo.model.erp.warehouse.GoodsAllocation;
import com.zoo.model.erp.warehouse.Stock;
import com.zoo.model.erp.warehouse.StockDetail;
import com.zoo.model.system.user.UserInfo;
import com.zoo.service.erp.JournalAccountService;
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
	private OutboundDetailMapper outboundDetailMapper;
	
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
		if(task.getTaskDefinitionKey().equals("assembledckzg")) {
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
	
	public void addInbound(Inbound inbound, String goodsAllocationId, BigDecimal number) {
		//获取入库单
		Inbound ib = inboundMapper.getInboundByForeignKey(inbound.getForeignKey());
		//获取组装单
		ProductAssembled assembled = this.getProductAssembledById(inbound.getForeignKey());
		//获取货位
		GoodsAllocation goodsAllocation = gaMapper.getGoodsAllocationById(goodsAllocationId);
		
		InboundDetail inboundDetail = new InboundDetail();
		if(ib != null) {
			for(InboundDetail detail: ib.getDetails()) {
				if(detail.getGoodsAllocation().getId().equals(goodsAllocationId)) {
					detail.setNumber(detail.getNumber().add(number));
					inboundDetailMapper.update(detail);
					break;
				}else if (!detail.getGoodsAllocation().getId().equals(goodsAllocation)) {
					inboundDetail.setId(UUID.randomUUID().toString());
					inboundDetail.setCtime(new Date());
					inboundDetail.setGoodsAllocation(goodsAllocation);
					inboundDetail.setNumber(number);
					inboundDetail.setInboundId(ib.getId());
					inboundDetail.setProduct(assembled.getProduct());
					inboundDetailMapper.addDetail(inboundDetail);
					break;
				}
			}
		}else {
			inbound.setId(UUID.randomUUID().toString());
			inbound.setCode(assembled.getCode());
			inbound.setType("ZZ");
			inbound.setCuserId(LoginInterceptor.getLoginUser().getId());
			inbound.setCtime(new Date());
			inbound.setWarehouse(assembled.getWarehouse());
			inboundMapper.addInbound(inbound);
			inboundDetail.setId(UUID.randomUUID().toString());
			inboundDetail.setCtime(new Date());
			inboundDetail.setGoodsAllocation(goodsAllocation);
			inboundDetail.setNumber(number);
			inboundDetail.setInboundId(inbound.getId());
			inboundDetail.setProduct(assembled.getProduct());
			inboundDetailMapper.addDetail(inboundDetail);
		}
		/*--------------更新仓库库存开始----------------*/
		Stock stock = stockMapper.getStock(assembled.getProduct().getId(), assembled.getWarehouse().getId());
		
		if(stock != null) {
			//更新后使用数量
			BigDecimal after_usableNumber = stock.getUsableNumber().add(number);
			//更新后总额
			BigDecimal after_totalMoney = stock.getTotalMoney().add(number.multiply(stock.getCostPrice()));
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
			stockDetail.setUsableNumber(stockDetail.getUsableNumber().add(number));
			stockDetailMapper.updateStockDetail(stockDetail);
		}
		/*--------------更新货位库存结束----------------*/
		
		/*------------------库存变动明细开始----------------------*/
		JournalAccount journalAccount = new JournalAccount();
		journalAccount.setId(UUID.randomUUID().toString());
		journalAccount.setType(JournalAccountType.ASSEMBLED);
		journalAccount.setOrderCode(assembled.getCode());
		journalAccount.setOrderDetailId(assembled.getId());
		journalAccount.setStock(stock);
		journalAccount.setCkNumber(stock.getUsableNumber());
		journalAccount.setCkPrice(stock.getCostPrice());
		journalAccount.setTotalMoney(stock.getTotalMoney());
		journalAccount.setCtime(new Date());
		journalAccount.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
		journalAccount.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		journalAccountService.addJournalAccount(journalAccount);
		/*------------------库存变动明细结束----------------------*/
	}
	
	/**
	 * 作废
	 * @param id
	 */
	public void destroy(String id) {
		//获取组装单
		ProductAssembled assembled = this.getProductAssembledById(id);
		String status = assembled.getStatus();
		//获取组装入库单
		Inbound inbound = inboundMapper.getInboundByForeignKey(id);
		if(status.equals(ProductAssembledStatus.FINISHED)) {//如果是完成状态
			Stock stock = stockMapper.getStock(assembled.getProduct().getId(), assembled.getWarehouse().getId());
			//设置库存可用数量
			if(stock.getUsableNumber().subtract(assembled.getNumber()).compareTo(BigDecimal.ZERO) == 1) {
				throw new ZooException(ExceptionEnum.STOCK_NOT_ENOUGH);//库存不足
			}else {
				//设置库存可用数量
				stock.setUsableNumber(stock.getUsableNumber().subtract(assembled.getNumber()));
				//设置库存总额
				stock.setTotalMoney(stock.getTotalMoney().add(assembled.getNumber().multiply(stock.getCostPrice())));
				stockMapper.updateStock(stock);
			}
			for(InboundDetail inboundDetail: inbound.getDetails()) {
				//获取库存详情表
				StockDetail stockDetail = stockDetailMapper.getDetail(stock.getId(), inboundDetail.getGoodsAllocation().getId());
				if(inboundDetail.getNumber().subtract(assembled.getNumber()).compareTo(BigDecimal.ZERO) == 1) {
					throw new ZooException(ExceptionEnum.STOCK_DETAIL_NO_ENOUGH);//货位库存不足
				}else {
					//设置货位可用数量
					stockDetail.setUsableNumber(inboundDetail.getNumber().subtract(assembled.getNumber()));
					stockDetailMapper.updateStockDetail(stockDetail);
				}
			}
			//添加拆分出库的库存变动明细
			JournalAccount account = new JournalAccount();
			account.setId(UUID.randomUUID().toString());
			account.setType(JournalAccountType.ASSEMBLED);
			account.setOrderCode(assembled.getCode());
			account.setOrderDetailId(assembled.getId());
			account.setCkNumber(assembled.getNumber());
			account.setCkPrice(stock.getCostPrice());
			account.setTotalMoney(stock.getTotalMoney());
			account.setCtime(new Date());
			account.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
			account.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
			journalAccountService.addJournalAccount(account);
			
			for(ProductAssembledMaterial material: assembled.getMaterials()) {
				Outbound outbound = outboundMapper.getOutboundByForeignKey(material.getId());
				Stock stock2 = stockMapper.getStock(material.getProduct().getId(), assembled.getWarehouse().getId());
				BigDecimal usableNumber = stock2.getUsableNumber();
				stock2.setUsableNumber(usableNumber.add(material.getNumber()));
				stock2.setTotalMoney(stock2.getTotalMoney().add(material.getNumber().multiply(stock2.getCostPrice())));
				stockMapper.updateStock(stock2);
				for(OutboundDetail outboundDetail: outbound.getDetails()) {
					StockDetail stockDetail = stockDetailMapper.getDetail(stock2.getId(), outboundDetail.getGoodsAllocation().getId());
					BigDecimal usableNumber2 = stockDetail.getUsableNumber();
					stockDetail.setUsableNumber(usableNumber2.add(outboundDetail.getNumber()));
					stockDetailMapper.updateStockDetail(stockDetail);
				}
			}
		}
	}
}
