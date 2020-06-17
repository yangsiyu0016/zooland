package com.zoo.service.erp.aftersales.changeorder;

import java.math.BigDecimal;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zoo.mapper.erp.aftersales.changeorder.ChangeOrderDetailMapper;
import com.zoo.model.erp.aftersales.changeorder.ChangeOrderDetail;

@Service
public class ChangeOrderDetailService {

	@Autowired
	ChangeOrderDetailMapper detailMapper;
	/**
	 * 添加数据
	 * @param detail
	 * @return
	 */
	public ChangeOrderDetail addDetail(ChangeOrderDetail detail) {
		
		detail.setId(UUID.randomUUID().toString());
		
		detailMapper.addDetail(detail);
		
		return detail;
		
	}
	
	/**
	 * 更新数据
	 * @param detail
	 * @return
	 */
	public int updateDetail(ChangeOrderDetail detail) {
		
		int num = detailMapper.updateDetail(detail);
		
		return num;
		
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
	 * 更新换货数量
	 * @param id
	 * @param number
	 * @return
	 */
	public int updateNumberById(String id, BigDecimal number) {
		
		int num = detailMapper.updateNumberById(number, id);
		
		return num;
	}
	
	/**
	 * 根据换货单id获取数据
	 * @param changeOrderId
	 * @return
	 */
	public List<ChangeOrderDetail> getDetailByChangeOrderId(String changeOrderId) {
		
		List<ChangeOrderDetail> list = detailMapper.getDetailByChangeOrderId(changeOrderId);
		return list;
	}
	
}
