package com.zoo.mapper.erp.aftersales.returnorder;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.aftersales.returnorder.ReturnOrder;

/**
 * 退货单
 * @author aa
 *
 */
@Component
public interface ReturnOrderMapper {

	/**
	 * 分页查询退货单
	 */
	List<ReturnOrder> getReturnOrderByPage(@Param("start") Integer start, @Param("size") Integer size, @Param("companyId") String companyId);
	
	/**
	 * 获取退货单总条数
	 * @param companyId
	 * @return
	 */
	Long getCount(@Param("companyId") String companyId);
	
	/**
	 * 根据id获取退货单
	 * @param id
	 * @return
	 */
	ReturnOrder getReturnOrderById(@Param("id") String id);
	
	/**
	 * 添加退货单
	 * @param returnOrder
	 */
	void addReturnOrder(@Param("returnOrder") ReturnOrder returnOrder);
	
	/**
	 * 更新退货单
	 * @param returnOrder
	 * @return
	 */
	int updateReturnOrder(@Param("returnOrder") ReturnOrder returnOrder);
	
	/**'
	 * 根据id更改流程id
	 * @param processInstanceId
	 * @param id
	 * @return
	 */
	int updateProcessInstanceId(@Param("processInstanceId") String processInstanceId, @Param("id") String id);
	
	/**
	 * 更新流程状态和是否被签收
	 * @param condition
	 * @return
	 */
	int updateReturnOrderStatus(@Param("condition") Map<String, Object> condition);
	
	/**
	 * 更新退货单是否被签收
	 * @param condition
	 * @return
	 */
	int updateReturnOrderIsClaimed(@Param("condition") Map<String, Object> condition);
	
	/**
	 * 批量删除退货单
	 * @param ids
	 * @return
	 */
	int deleteReturnOrderById(@Param("ids") String[] ids);
	
}
