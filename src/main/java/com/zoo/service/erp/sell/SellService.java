package com.zoo.service.erp.sell;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.controller.erp.constant.SellStatus;
import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.product.SpecParamMapper;
import com.zoo.mapper.erp.sell.SellDetailMapper;
import com.zoo.mapper.erp.sell.SellMapper;
import com.zoo.model.erp.product.ProductSku;
import com.zoo.model.erp.product.SpecParam;
import com.zoo.model.erp.sell.Sell;
import com.zoo.model.erp.sell.SellDetail;
import com.zoo.model.system.user.UserInfo;
import com.zoo.utils.OrderCodeHelper;

import net.sf.json.JSONObject;

@Service
@Transactional
public class SellService {
	@Autowired
	SellMapper sellMapper;
	@Autowired
	SellDetailMapper detailMapper;
	@Autowired
	SpecParamMapper paramMapper;
	@Autowired
	IdentityService identityService;
	@Autowired
	ProcessEngine processEngine;
	public void addSell(Sell sell) {
		String id = UUID.randomUUID().toString();
		sell.setId(id);
		if(sell.getCodeGeneratorType().equals("AUTO")) {
			try {
				sell.setCode(OrderCodeHelper.GenerateId("XS"));
			} catch (Exception e) {
				throw new ZooException(ExceptionEnum.GENER_CODE_ERROR);
			}
		}
		sell.setStatus(SellStatus.WTJ);
		sell.setCuserId(LoginInterceptor.getLoginUser().getId());
		sell.setCtime(new Date());
		sell.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		sellMapper.addSell(sell);
		
		for(SellDetail detail:sell.getDetails()) {
			detail.setId(UUID.randomUUID().toString());
			detail.setSellId(id);
			detailMapper.addDetail(detail);
		}
	}
	public List<Sell> getSellByPage(Integer page, Integer size) {
		Integer start = (page-1)*size;
		List<Sell> sells = sellMapper.getSellByPage(start,size,LoginInterceptor.getLoginUser().getCompanyId());
		return sells;
	}
	public long getCount() {
		// TODO Auto-generated method stub
		return sellMapper.getCount(LoginInterceptor.getLoginUser().getCompanyId());
	}
	public void deleteSellById(String ids) {
		String[] split = ids.split(",");
		for(String sellId:split) {
			detailMapper.deleteDetailBySellId(sellId);
		}
		sellMapper.deleteSellById(split);
		
	}
	public Sell getSellById(String id) {
		Sell sell = sellMapper.getSellById(id);
		for(SellDetail detail:sell.getDetails()) {
			ProductSku sku = detail.getProductSku();
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
		}
		return sell;
	}
	public void startProcess(String id) {
		Sell sell = this.getSellById(id);
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
}