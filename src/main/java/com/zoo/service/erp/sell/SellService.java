package com.zoo.service.erp.sell;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.util.StringUtil;
import com.zoo.controller.erp.constant.SellStatus;
import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.annex.AnnexMapper;
import com.zoo.mapper.erp.sell.SellDetailMapper;
import com.zoo.mapper.erp.sell.SellMapper;
import com.zoo.model.annex.Annex;
import com.zoo.model.erp.cost.Cost;
import com.zoo.model.erp.sell.Sell;
import com.zoo.model.erp.sell.SellDetail;
import com.zoo.model.system.user.UserInfo;
import com.zoo.service.annex.AnnexService;
import com.zoo.service.erp.cost.CostService;
import com.zoo.service.system.parameter.SystemParameterService;
import com.zoo.utils.CodeGenerator;

@Service
@Transactional
public class SellService {
	@Autowired
	SellMapper sellMapper;
	@Autowired
	SellDetailMapper detailMapper;
	@Autowired
	IdentityService identityService;
	@Autowired
	ProcessEngine processEngine;
	@Autowired
	AnnexMapper annexMapper;
	@Autowired
	CostService costService;
	@Autowired
	AnnexService annexService;
	@Autowired
	SystemParameterService systemParameterService;
	public void addSell(Sell sell) {
		String id = UUID.randomUUID().toString();
		sell.setId(id);
		if(sell.getCodeGeneratorType().equals("AUTO")) {
			try {
				String parameterValue = systemParameterService.getValueByCode("c00002");
				String code = CodeGenerator.getInstance().generator(parameterValue);
				sell.setCode(code);
			} catch (Exception e) {
				throw new ZooException(ExceptionEnum.GENER_CODE_ERROR);
			}
		}
		sell.setStatus(SellStatus.WTJ);
		sell.setCuserId(LoginInterceptor.getLoginUser().getId());
		sell.setCtime(new Date());
		sell.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		sell.setReceivingContext(sell.getReceiving().getProvince().getName()+
				sell.getReceiving().getCity().getName()+
				sell.getReceiving().getCounty().getName()+
				sell.getReceiving().getAddress()+" | "+
				sell.getReceiving().getConsignee()+" | "+sell.getReceiving().getCellphone());
		sellMapper.addSell(sell);
		
		for(SellDetail detail:sell.getDetails()) {
			detail.setId(UUID.randomUUID().toString());
			detail.setSellId(id);
			detail.setCtime(new Date());
			detailMapper.addDetail(detail);
		}
		for(Annex annex:sell.getAnnexs()) {
			annex.setId(UUID.randomUUID().toString());
			annex.setForeignKey(sell.getId());
			annexMapper.addAnnex(annex);
		}
	}
	public List<Sell> getSellByPage(
			Integer page, Integer size,
			String cuserId,String keywords,
			String code,String productCode,
			String productName,String customerName,
			String start_initDate,String end_initDate,
			String start_ctime,String end_ctime,
			String status,String sort,String order) {
		Integer start = (page-1)*size;
		List<Sell> sells = sellMapper.getSellByPage(start,size,
				LoginInterceptor.getLoginUser().getCompanyId(),cuserId,
				keywords,code,
				productCode,productName,
				customerName,start_initDate,end_initDate,start_ctime,end_ctime,status.split(","),sort,order);
		return sells;
	}
	public long getCount(String cuserId,String keywords,
			String code,String productCode,
			String productName,String customerName,
			String start_initDate,String end_initDate,
			String start_ctime,String end_ctime,
			String status) {
		// TODO Auto-generated method stub
		return sellMapper.getCount(LoginInterceptor.getLoginUser().getCompanyId(),cuserId,keywords,code,
				productCode,productName,
				customerName,start_initDate,end_initDate,start_ctime,end_ctime,status.split(","));
	}
	public void deleteSellById(String ids) {
		String[] split = ids.split(",");
		for(String sellId:split) {
			//删除产品详情
			detailMapper.deleteDetailBySellId(sellId);
			//删除物流信息
			costService.deleteByForeignKey(sellId);
			//删除附件
			annexService.delAnnexByForeignKey(sellId);
		}
		sellMapper.deleteSellById(split);
		
	}
	public Sell getSellById(String id) {
		Sell sell = sellMapper.getSellById(id);
		return sell;
	}
	public void startProcess(String id) {
		Sell sell = this.getSellById(id);
		if(StringUtil.isNotEmpty(sell.getProcessInstanceId())) {
			throw new ZooException(ExceptionEnum.FLOWSTATED);
		}
		UserInfo user = LoginInterceptor.getLoginUser();
		Map<String, Object> variables=new HashMap<String,Object>();
		// 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
        identityService.setAuthenticatedUserId(user.getId());
		String businessKey = id;
		variables.put("CODE", sell.getCode());
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance processInstance = runtimeService
        		.startProcessInstanceByKeyAndTenantId("sell",businessKey,variables, user.getCompanyId());
        String processInstanceId = processInstance.getId();
        this.sellMapper.updateProcessInstanceId(id, processInstanceId);
        Map<String,Object> condition = new HashMap<String,Object>();
        condition.put("id", id);
		condition.put("status", SellStatus.CWSH);
		this.updateSellStatus(condition);
	}
	public int  updateSellStatus(Map<String, Object> condition) {
		return this.sellMapper.updateSellStatus(condition);
		
	}
	public int updateSell(Sell sell) {
		sell.setReceivingContext(sell.getReceiving().getProvince().getName()+
				sell.getReceiving().getCity().getName()+
				sell.getReceiving().getCounty().getName()+
				sell.getReceiving().getAddress()+" | "+
				sell.getReceiving().getConsignee()+" | "+sell.getReceiving().getCellphone());

		return sellMapper.updateSell(sell);
		
	}
	public void destroy(String id) {
		// TODO Auto-generated method stub
		
		Sell sell = sellMapper.getSellById(id);
		String status = sell.getStatus();
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("id", id);
		condition.put("status", SellStatus.DESTROY);
		condition.put("etime", new Date());
		sellMapper.updateSellStatus(condition);
		//删除流程
		if(StringUtil.isNotEmpty(sell.getProcessInstanceId())&&!status.equals(SellStatus.WTJ)&&!status.equals(SellStatus.FINISHED)) {
			RuntimeService runtimeService = processEngine.getRuntimeService();
			runtimeService.deleteProcessInstance(sell.getProcessInstanceId(), "待定");
			
		}
		
		sellMapper.updateProcessInstanceId(id, null);
		
		
		//设置是否被签收表示
		Map<String,Object> isClaimedCondition = new HashMap<String,Object>();
		isClaimedCondition.put("code", sell.getCode());
		isClaimedCondition.put("isClaimed", "N");
		
		sellMapper.updateSellIsClaimed(isClaimedCondition);
		//删除物流信息
		List<Cost> costs = costService.getCostByForeignKey(id);
		for(Cost cost:costs) {
			costService.deleteCostFromSell(cost.getId(),"DESTROY");
		}
		//for(Annex annex:sell.getAnnexs()) {
		//	annexService.delAnnexFile(annex);
		//}
	}
	//流程取回
	public void reset(String id) {
		// TODO Auto-generated method stub
		Sell sell = this.getSellById(id);
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("id", id);
		condition.put("status", SellStatus.WTJ);
		condition.put("isClaimed", "N");//设置是否签收
		sellMapper.updateSellStatus(condition);
		
		//删除流程
		RuntimeService runtimeService = processEngine.getRuntimeService();
		runtimeService.deleteProcessInstance(sell.getProcessInstanceId(), "待定");
		
		sellMapper.updateProcessInstanceId(id, null);
	}
	public void updateSellIsClaimed(Map<String, Object> variables) {
		// TODO Auto-generated method stub
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("id", variables.get("id"));
		condition.put("isClaimed", "Y");
		sellMapper.updateSellIsClaimed(condition);
	}
	
	
	
}
