package com.zoo.service.erp.aftersales.repairorder;

import java.util.Date;
import java.util.UUID;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.controller.erp.constant.RepairOrderStatus;
import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.aftersales.repairorder.RepairOrderDetailMapper;
import com.zoo.mapper.erp.aftersales.repairorder.RepairOrderMapper;
import com.zoo.mapper.erp.product.SpecParamMapper;
import com.zoo.model.erp.aftersales.repairorder.RepairOrder;
import com.zoo.model.erp.aftersales.repairorder.RepairOrderDetail;
import com.zoo.utils.OrderCodeHelper;

@Service
public class RepairOrderService {

	@Autowired
	RepairOrderMapper repairOrderMapper;
	
	@Autowired
	RepairOrderDetailMapper detailMapper;
	
	@Autowired
	SpecParamMapper paramMapper;
	
	@Autowired
	ProcessEngine processEngine;
	
	@Autowired
	IdentityService identityService;
	
	/**
	 * 添加
	 */
	public void addRepairOrder(RepairOrder order) {
		String id = UUID.randomUUID().toString();
		
		order.setId(id);
		if("AUTO".equals(order.getCodeGeneratorType())) {
			try {
				order.setCode(OrderCodeHelper.GenerateId("WX"));
			} catch (Exception e) {
				// TODO: handle exception
				throw new ZooException(ExceptionEnum.GENER_CODE_ERROR);
			}
		}
		order.setStatus(RepairOrderStatus.WTJ);
		order.setCuserId(LoginInterceptor.getLoginUser().getId());
		order.setCtime(new Date());
		order.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		order.setIsClaimed("N");
		repairOrderMapper.addRepairOrder(order);
		
		for(RepairOrderDetail detail: order.getDetails()) {
			detail.setId(UUID.randomUUID().toString());
			detail.setRepairOrderId(id);
			detail.setCtime(new Date());
			detailMapper.addDetail(detail);
		}
	}
	
}
