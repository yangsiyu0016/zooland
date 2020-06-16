package com.zoo.service.erp.openingInventory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.util.StringUtil;
import com.zoo.controller.erp.constant.JournalAccountType;
import com.zoo.controller.erp.constant.OpeningInventoryStatus;
import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.openingInventory.OpeningInventoryDetailMapper;
import com.zoo.mapper.erp.openingInventory.OpeningInventoryMapper;
import com.zoo.mapper.erp.product.SpecParamMapper;
import com.zoo.model.erp.JournalAccount;
import com.zoo.model.erp.openingInventory.OpeningInventory;
import com.zoo.model.erp.openingInventory.OpeningInventoryDetail;
import com.zoo.model.erp.product.ProductSku;
import com.zoo.model.erp.product.SpecParam;
import com.zoo.model.erp.warehouse.Stock;
import com.zoo.model.erp.warehouse.StockDetail;
import com.zoo.model.system.user.UserInfo;
import com.zoo.service.erp.JournalAccountService;
import com.zoo.service.erp.warehouse.StockDetailService;
import com.zoo.service.erp.warehouse.StockService;
import com.zoo.service.system.parameter.SystemParameterService;
import com.zoo.utils.CodeGenerator;
import com.zoo.utils.OrderCodeHelper;

import net.sf.json.JSONObject;

@Service
@Transactional
public class OpeningInventoryService {
	@Autowired
	OpeningInventoryMapper openingInventoryMapper;
	@Autowired
	SpecParamMapper paramMapper;
	@Autowired
	OpeningInventoryDetailMapper detailMapper;
	@Autowired
	ProcessEngine processEngine;
	@Autowired
	StockService stockService;
	@Autowired
	StockDetailService stockDetailService;
	@Autowired
	IdentityService identityService;
	@Autowired
	JournalAccountService journalAccountService;
	@Autowired
	SystemParameterService systemParameterService;
	public List<OpeningInventory> getOpeningInventoryByPage(Integer page,Integer size,String cuserId){
		int start = (page-1)*size;
		List<OpeningInventory> ois = openingInventoryMapper.getOpeningInventoryByPage(start, size, LoginInterceptor.getLoginUser().getCompanyId(), cuserId);
		//ois = buildSpec(ois);
		return ois;
	}
	private OpeningInventory buildSpec(OpeningInventory oi) {
		List<String> built = new ArrayList<String>();
		for(OpeningInventoryDetail detail:oi.getDetails()) {
			ProductSku sku = detail.getProductSku();
			if(!built.contains(sku.getProduct().getId())){
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
		return oi;
	}
	public Long getCount(String cuserId) {
		return openingInventoryMapper.getCount(LoginInterceptor.getLoginUser().getCompanyId(), cuserId);
	}
	public OpeningInventory getOpeningInventoryById(String id) {
		OpeningInventory oi = openingInventoryMapper.getOpeningInventoryById(id);
		return buildSpec(oi);
	}
	public void addOpeningInventory(OpeningInventory oi) {
		String id = UUID.randomUUID().toString();
		oi.setId(id);
		if(oi.getCodeGeneratorType().equals("AUTO")) {
			try {
				String parameterValue = systemParameterService.getValueByCode("C00003");
				String code = CodeGenerator.getInstance().generator(parameterValue);
				oi.setCode(code);
			} catch (Exception e) {
				throw new ZooException(ExceptionEnum.GENER_CODE_ERROR);
			}
		}
		oi.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		oi.setCuserId(LoginInterceptor.getLoginUser().getId());
		oi.setStatus(OpeningInventoryStatus.WTJ);
		oi.setCtime(new Date());
		openingInventoryMapper.addOpeningInventory(oi);
		
		for(OpeningInventoryDetail detail:oi.getDetails()) {
			detail.setId(UUID.randomUUID().toString());
			detail.setCtime(new Date());
			detail.setOpeningInventoryId(id);
			detailMapper.addDetail(detail);
		}
	}
	public void startProcess(String id) {
		OpeningInventory openingInventory = this.getOpeningInventoryById(id);
		UserInfo user = LoginInterceptor.getLoginUser();
		Map<String, Object> variables=new HashMap<String,Object>();
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
        identityService.setAuthenticatedUserId(user.getId());
		String businessKey = id;
		variables.put("CODE", openingInventory.getCode());
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance processInstance = runtimeService
        		.startProcessInstanceByKeyAndTenantId("openingInventory",businessKey,variables, user.getCompanyId());
        String processInstanceId = processInstance.getId();
        this.openingInventoryMapper.updateProcessInstanceId(id, processInstanceId);
        Map<String,Object> condition = new HashMap<String,Object>();
        condition.put("id", id);
		condition.put("status", OpeningInventoryStatus.ZGSH);
		this.updateOpeningInventoryStatus(condition);
	}
	public int  updateOpeningInventoryStatus(Map<String, Object> condition) {
		return this.openingInventoryMapper.updateOpeningInventoryStatus(condition);
		
	}
	public void doCallBackFlow(String id) {
		OpeningInventory openingInventory = this.getOpeningInventoryById(id);
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("id", id);
		condition.put("status", OpeningInventoryStatus.WTJ);
		this.updateOpeningInventoryStatus(condition);
		openingInventoryMapper.updateProcessInstanceId(id, null);
		RuntimeService runtimeService = processEngine.getRuntimeService();
		runtimeService.deleteProcessInstance(openingInventory.getProcessInstanceId(), "待定");
		
	}
	public int updateOpeningInventory(OpeningInventory oi) {
		return openingInventoryMapper.updateOpeningInventory(oi);
		
	}
	public void updateOpeningInventoryIsClaimed(Map<String, Object> variables) {
		// TODO Auto-generated method stub
		Map<String, Object> condition = new HashMap<String, Object>();
		// TODO Auto-generated method stub
		
		condition.put("id", variables.get("id"));
		condition.put("isClaimed", "Y");
		openingInventoryMapper.updateOpeningInventoryIsClaimed(condition);
	}
	public void destroy(String id) {
		// TODO Auto-generated method stub
		OpeningInventory openingInventory = openingInventoryMapper.getOpeningInventoryById(id);
		String status = openingInventory.getStatus();
		
		if(status.equals(OpeningInventoryStatus.FINISHED)) {
			for(OpeningInventoryDetail oiDetail:openingInventory.getDetails()) {
				Stock stock = stockService.getStock(oiDetail.getProductSku().getId(), openingInventory.getWarehouse().getId());
				BigDecimal usableNumber = stock.getUsableNumber();
				if(oiDetail.getNumber().subtract(usableNumber).compareTo(BigDecimal.ZERO)==1) {//库存不足
					throw new ZooException(ExceptionEnum.STOCK_NOT_ENOUGH);
				}else {
					StockDetail stockDetail =  stockDetailService.getStockDetail(stock.getId(), oiDetail.getGoodsAllocation().getId());
					BigDecimal detailUsableNumber = stockDetail.getUsableNumber();
					if(oiDetail.getNumber().subtract(detailUsableNumber).compareTo(BigDecimal.ZERO)==1) {//货位库存不足
						throw new ZooException(ExceptionEnum.STOCK_DETAIL_NO_ENOUGH);
					}else {
						stockDetail.setUsableNumber(detailUsableNumber.subtract(oiDetail.getNumber()));
						stockDetailService.updateStockDetail(stockDetail);
						BigDecimal after_usableNumber = usableNumber.subtract(oiDetail.getNumber());
						BigDecimal after_totalMoney;
						BigDecimal after_costPrice;
						stock.setUsableNumber(after_usableNumber);
						
						if(after_usableNumber.compareTo(BigDecimal.ZERO)==0) {
							after_totalMoney = new BigDecimal("0");
							after_costPrice = stock.getCostPrice();
						}  else {
							after_totalMoney = stock.getTotalMoney().subtract(oiDetail.getTotalMoney());
							after_costPrice = after_totalMoney.divide(after_usableNumber,4,BigDecimal.ROUND_HALF_UP);
						}
						
						stock.setCostPrice(after_costPrice);
						stock.setTotalMoney(after_totalMoney);
						
						
						JournalAccount journalAccount = new JournalAccount();
						journalAccount.setId(UUID.randomUUID().toString());
						journalAccount.setType(JournalAccountType.QCDESTROY);
						journalAccount.setOrderDetailId(oiDetail.getId());
						journalAccount.setOrderCode(openingInventory.getCode());
						journalAccount.setStock(stock);
						journalAccount.setCkNumber(oiDetail.getNumber());
						journalAccount.setCkPrice(oiDetail.getCostPrice());
						journalAccount.setCkTotalMoney(oiDetail.getTotalMoney());
						journalAccount.setCtime(new Date());
						journalAccount.setTotalNumber(stock.getUsableNumber().add(stock.getLockedNumber()==null?new BigDecimal("0"):stock.getLockedNumber()));
						journalAccount.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
						journalAccountService.addJournalAccount(journalAccount);
					}
				}
			}
		}
		
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("id", id);
		condition.put("status", OpeningInventoryStatus.DESTROY);
		condition.put("etime", new Date());
		openingInventoryMapper.updateOpeningInventoryStatus(condition);
		if(StringUtil.isNotEmpty(openingInventory.getProcessInstanceId())&&!status.equals(OpeningInventoryStatus.WTJ)&&!status.equals(OpeningInventoryStatus.FINISHED)) {
			//删除流程
			RuntimeService runtimeService = processEngine.getRuntimeService();
			runtimeService.deleteProcessInstance(openingInventory.getProcessInstanceId(), "待定");

		}
		
		openingInventoryMapper.updateProcessInstanceId(id, null);
		//设置是否被签收表示
		Map<String,Object> isClaimedCondition = new HashMap<String,Object>();
		isClaimedCondition.put("code", openingInventory.getCode());
		isClaimedCondition.put("isClaimed", "N");
		
		openingInventoryMapper.updateOpeningInventoryIsClaimed(isClaimedCondition);
		
		
	}
	//流程取回
	public void reset(String id) {
		// TODO Auto-generated method stub
		OpeningInventory openingInventory = this.getOpeningInventoryById(id);
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("id", id);
		condition.put("status", OpeningInventoryStatus.WTJ);
		condition.put("isClaimed", "N");//设置是否签收
		openingInventoryMapper.updateOpeningInventoryStatus(condition);
		
		//删除流程
		RuntimeService runtimeService = processEngine.getRuntimeService();
		runtimeService.deleteProcessInstance(openingInventory.getProcessInstanceId(), "待定");
		
		openingInventoryMapper.updateProcessInstanceId(id, null);
	}
}
