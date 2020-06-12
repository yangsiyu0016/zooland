package com.zoo.mapper.erp.aftersales.changeorder;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
/**
 * 换货单
 * @author aa
 *
 */

import com.zoo.model.erp.aftersales.changeorder.ChangeOrder;
@Component
public interface ChangeOrderMapper {
	
	/**
	 * 分页查询换货单
	 * @param start
	 * @param size
	 * @param companyId
	 * @return
	 */
	List<ChangeOrder> getChangeOrderByPage(@Param("start") Integer start, @Param("size") Integer size, @Param("companyId") String companyId);
	
	/**
	 * 获取数据总条数
	 */
	Long getCount(@Param("companyId") String companyId);
	
	/**
	 * 根据id获取换货单
	 * @param changeOrder
	 * @return
	 */
	ChangeOrder getChangeOrderById(@Param("id") String id);
	
	/**
	 * 添加换货单
	 * @param changeOrder
	 */
	void addChangeOrder(@Param("changeOrder") ChangeOrder changeOrder);
	
	/**
	 * 更改换货单
	 * @param changeOrder
	 * @return
	 */
	int updateChangeOrder(@Param("changeOrder") ChangeOrder changeOrder);
	
	/**
	 * 更新流程id
	 * @param processInstanceId
	 * @param id
	 * @return
	 */
	int updateProcessInstanceId(@Param("processInstanceId") String processInstanceId, @Param("id") String id);
	
	/**
	 * 更新流程id
	 * @param condition
	 * @return
	 */
	int updateChangeOrderStatus(@Param("condition") Map<String, Object> condition);
	
	/**
	 * 更新是否签收
	 * @param condition
	 * @return
	 */
	int updateChangeOrderIsClaimed(@Param("condition") Map<String, Object> condition);
	
	/**
	 * 批量删除换货单
	 * @param ids
	 * @return
	 */
	int deleteChangeOrderById(@Param("ids") String[] ids);
}
