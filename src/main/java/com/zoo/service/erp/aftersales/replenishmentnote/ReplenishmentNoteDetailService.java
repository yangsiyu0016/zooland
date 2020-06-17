package com.zoo.service.erp.aftersales.replenishmentnote;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.erp.aftersales.replenishmentnote.ReplenishmentNoteDetailMapper;
import com.zoo.model.erp.aftersales.replenishmentnote.ReplenishmentNoteDetail;
@Service
public class ReplenishmentNoteDetailService {

	@Autowired
	ReplenishmentNoteDetailMapper detailMapper;
	/**
	 * 添加数据
	 * @param detail
	 * @return
	 */
	public ReplenishmentNoteDetail addDatail(ReplenishmentNoteDetail detail) {
		detail.setId(UUID.randomUUID().toString());
		
		detailMapper.addDetail(detail);
		return detail;
	}
	
	/**
	 * 更新数据
	 * @param detail
	 * @return
	 */
	public int updateDetail(ReplenishmentNoteDetail detail) {
		return detailMapper.updateDetail(detail);
	}
	
	/**
	 * 批量删除数据
	 * @param ids
	 * @return
	 */
	public int deleteDetailById(String ids) {
		String[] split = ids.split(",");
		int num = detailMapper.deleteDetailById(split);
		return num;
	}
	
	/**
	 * 更新补货数量，单价，总额
	 * @param id
	 * @param number
	 * @return
	 */
	public int updateNumberById(String id, ReplenishmentNoteDetail detail) {
		BigDecimal number = detail.getNumber();
		BigDecimal costPrice = detail.getCostPrice();
		BigDecimal totalMoney = number.multiply(costPrice);
		return detailMapper.updateNumberById(id, number, costPrice, totalMoney);
	}
	
	/**
	 * 根据补货单id获取数据
	 * @param changeOrderId
	 * @return
	 */
	public List<ReplenishmentNoteDetail> getDetailByReplenishmentNoteId(String replenishmentNoteId) {
		List<ReplenishmentNoteDetail> list = detailMapper.getDetailByReplenishmentNoteId(replenishmentNoteId);
		return list;
	}
	
}
