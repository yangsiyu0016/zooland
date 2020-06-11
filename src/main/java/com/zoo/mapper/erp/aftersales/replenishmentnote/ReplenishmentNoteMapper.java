package com.zoo.mapper.erp.aftersales.replenishmentnote;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.aftersales.replenishmentnote.ReplenishmentNote;

/**
 * 补货单
 * @author aa
 *
 */
@Component
public interface ReplenishmentNoteMapper {

	/**
	 * 分页查询补货单
	 * @param start
	 * @param size
	 * @param companyId
	 * @return
	 */
	List<ReplenishmentNote> getReplenishmentNoteByPage(@Param("start") Integer start, @Param("size") Integer size, @Param("companyId") String companyId);
	
	/**
	 * 获取补货单总条数
	 * @param companyId
	 * @return
	 */
	Long getCount(@Param("companyId") String companyId);
	
	/**
	 * 根据id获取补货单
	 * @param id
	 * @return
	 */
	ReplenishmentNote getReplenishmentNoteById(@Param("id") String id);
	
	/**
	 * 添加补货单
	 * @param replenishmentNote
	 */
	void addReplenishmentNote(@Param("replenishmentNote") ReplenishmentNote replenishmentNote);
	
	/**
	 * 更新补货单
	 * @param replenishmentNote
	 * @return
	 */
	int updateReplenishmentNote(@Param("replenishmentNote") ReplenishmentNote replenishmentNote);
	
	/**
	 * 根据id修改流程id
	 * @param processInstanceId
	 * @param id
	 * @return
	 */
	int updateProcessInstanceId(@Param("processInstanceId") String processInstanceId, @Param("id") String id);
	
	/**
	 * 更新补货单流程状态和是否被签收
	 * @param condition
	 * @return
	 */
	int updateReplenishmentNoteStatus(@Param("condition") Map<String, Object> condition);
	
	/**
	 * 更改补货单是否被签收
	 * @param condition
	 * @return
	 */
	int updateReplenishmentNoteIsClaimed(@Param("condition") Map<String, Object> condition);
	
	/**
	 * 批量删除补货单
	 * @param ids
	 * @return
	 */
	int deleteReplenishmentNoteById(@Param("ids") String[] ids); 
	
}
