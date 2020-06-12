package com.zoo.mapper.erp.aftersales.returnorder;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.aftersales.returnorder.ReturnOrderDetail;

/**
 * 
 * @author aa
 *
 */
@Component
public interface ReturnOrderDetailMapper {

	/**
	 * 根据退货单获取数据
	 * @param returnOrderId
	 * @return
	 */
	List<ReturnOrderDetail> getDetailByReturnOrderId(@Param("returnOrderId") String returnOrderId);
	
	/**
	 * 根据id获取数据
	 * @param id
	 * @return
	 */
	ReturnOrderDetail getDetailById(@Param("id") String id);
	
	/**
	 * 添加数据
	 * @param detail
	 */
	void addDetail(@Param("detail") ReturnOrderDetail detail);
	
	/**
	 * 根据id更新数据
	 * @param detail
	 * @return
	 */
	int updateDetail(@Param("detail") ReturnOrderDetail detail);
	
	/**
	 * 根据退货单删除数据
	 * @param returnOrderId
	 * @return
	 */
	int deleteDetailByReturnOrderId(@Param("returnOrderId") String returnOrderId);
	
	/**
	 * 批量删除数据
	 * @param ids
	 * @return
	 */
	int deleteDetailById(@Param("ids") String[] ids);
	
	int updateNumberById(@Param("id") String id, @Param("number") BigDecimal number, @Param("CostPrice") BigDecimal costPrice, @Param("totalMoney") BigDecimal totalMoney);
	
}
