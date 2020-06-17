package com.zoo.service.erp.aftersales.returnorder;

import java.math.BigDecimal;

import java.util.List;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.erp.aftersales.returnorder.ReturnOrderDetailMapper;
import com.zoo.model.erp.aftersales.returnorder.ReturnOrderDetail;
@Service
public class ReturnOrderDetailService {

	@Autowired
	ReturnOrderDetailMapper detailMapper;
	/**
	 * 添加数据
	 * @param detail
	 * @return
	 */
	public ReturnOrderDetail addDetail(ReturnOrderDetail detail) {
		detail.setId(UUID.randomUUID().toString());
		
		detailMapper.addDetail(detail);
		
		return detail;
	}
	
	/**
	 * 更新数据
	 * @param detail
	 * @return
	 */
	public int updateDetail(ReturnOrderDetail detail) {
		return detailMapper.updateDetail(detail);
	}
	
	
	/**
	 * 批量删除数据
	 * @param ids
	 * @return
	 */
	public int deleteDetail(String ids) {
		String[] split = ids.split(",");
		
		int num = detailMapper.deleteDetailById(split);
		
		return num;
	}
	
	/**
	 * 更新退货数量，单价，总额
	 * @param id
	 * @param number
	 * @return
	 */
	public int updateNumberById(String id, ReturnOrderDetail detail) {
		BigDecimal number = detail.getNumber();
		BigDecimal costPrice = detail.getCostPrice();
		BigDecimal totalMoney = number.multiply(costPrice);
		return detailMapper.updateNumberById(id, number, costPrice, totalMoney);
		
	}
	
	/**
	 * 根据退货单id获取数据
	 * @param changeOrderId
	 * @return
	 */
	public List<ReturnOrderDetail> getDetailByReturnOrderId(String returnOrderId) {
		List<ReturnOrderDetail> list = detailMapper.getDetailByReturnOrderId(returnOrderId);
		return list;
	}
	
}
