package com.zoo.mapper.erp.aftersales.changeorder;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.aftersales.changeorder.ChangeOrderDetail;

/**
 * 换货单
 * @author aa
 *
 */
@Component
public interface ChangeOrderDetailMapper {

	/**
	 * 根据换货单id获取数据
	 * @param changeOrderId
	 * @return
	 */
	List<ChangeOrderDetail> getDetailByChangeOrderId(@Param("changeOrderId") String changeOrderId);
	
	/**
	 * 根据id获取数据
	 */
	ChangeOrderDetail getDetailById(@Param("id") String id);
	
	/**
	 * 添加数据
	 */
	void addDetail(@Param("detail") ChangeOrderDetail detail);
	
	/**
	 * 更新数据
	 */
	int updateDetail(@Param("detail") ChangeOrderDetail detail);
	
	/**
	 * 根据换货单id删除数据
	 */
	int deleteDetailByChangeOrderId(@Param("changeOrderId") String changeOrderId);
	/**
	 * 批量删除数据
	 */
	int deleteDetailById(@Param("ids")String[] ids);
	
	int updateNumberById(@Param("number") BigDecimal number, @Param("id") String id);
}
