package com.zoo.mapper.erp.aftersales.repairorder;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.aftersales.repairorder.RepairOrderDetail;

/**
 * 
 * @author aa
 *
 */
@Component
public interface RepairOrderDetailMapper {

	/**
	 * 根据维修单id获取数据
	 * @param repairOrderId
	 * @return
	 */
	List<RepairOrderDetail> getDetailByRepairOrderId(@Param("repairOrderId") String repairOrderId);
	
	/**
	 * 根据id获取数据
	 * @param id
	 * @return
	 */
	RepairOrderDetail getDetailById(@Param("id") String id);
	
	/**
	 * 添加数据
	 * @param detail
	 */
	void addDetail(@Param("detail") RepairOrderDetail detail); 
	
	/**
	 * 
	 * 更新数据
	 * @param detail
	 * @return
	 */
	int updateDetail(@Param("detail") RepairOrderDetail detail);
	
	/**
	 * 根据维修单id删除数据
	 * @param repairOrderId
	 * @return
	 */
	int deleteDetailByRepairOrderId(@Param("repairOrderId") String repairOrderId);
	
	/**
	 * 批量删除数据
	 * @param ids
	 * @return
	 */
	int deleteDetailById(@Param("ids") String[] ids);
	
	/**
	 * 根据id修改数量
	 * @param number
	 * @param id
	 * @return
	 */
	int updateNumberById(@Param("number") BigDecimal number, @Param("id") String id);
}
