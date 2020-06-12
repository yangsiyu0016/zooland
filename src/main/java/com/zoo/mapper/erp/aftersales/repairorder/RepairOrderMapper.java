package com.zoo.mapper.erp.aftersales.repairorder;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.aftersales.repairorder.RepairOrder;

/**
 * 维修单
 * @author aa
 *
 */
@Component
public interface RepairOrderMapper {

	/**
	 * 分页获取维修单
	 * @param start
	 * @param size
	 * @param companyId
	 * @return
	 */
	List<RepairOrder> getRepairOrderByPage(@Param("start") Integer start, @Param("size") Integer size, @Param("companyId") String companyId);
	
	/**
	 * 获取维修单总条数
	 * @param companyId
	 * @return
	 */
	Long getCount(@Param("companyId") String companyId);
	
	/**
	 * 根据id获取维修单
	 * @param id
	 * @return
	 */
	RepairOrder getRepairOrderById(@Param("id") String id);
	
	/**
	 * 添加维修单
	 * @param repairOrder
	 */
	void addRepairOrder(@Param("repairOrder") RepairOrder repairOrder);
	
	/**
	 * 更新维修单
	 * @param repairOrder
	 * @return
	 */
	int updateRepairOrder(@Param("repairOrder") RepairOrder repairOrder);
	
	/**
	 * 更新流程id
	 * @param processInstanceId
	 * @param id
	 * @return
	 */
	int updateProcessInstanceId(@Param("processInstanceId") String processInstanceId, @Param("id") String id);
	
	/**
	 * 更新维修单流程状态
	 * @param condition
	 * @return
	 */
	int updateRepairOrderStatus(@Param("condition") Map<String, Object> condition);
	
	/**
	 * 更新流程是否被签收
	 * @param condition
	 * @return
	 */
	int updateRepairOrderIsClaimed(@Param("condition") Map<String, Object> condition);
	
	/**
	 * 批量删除维修单
	 * @param ids
	 * @return
	 */
	int deleteRepairOrderById(@Param("ids") String[] ids);
}
