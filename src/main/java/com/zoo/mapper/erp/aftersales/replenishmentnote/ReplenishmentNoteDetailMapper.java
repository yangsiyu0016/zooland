package com.zoo.mapper.erp.aftersales.replenishmentnote;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.aftersales.replenishmentnote.ReplenishmentNoteDetail;

/**
 * 
 * @author aa
 *
 */
@Component
public interface ReplenishmentNoteDetailMapper {

	/**
	 * 根据换货单获取数据
	 * @param replenishmentNoteId
	 * @return
	 */
	List<ReplenishmentNoteDetail> getDetailByReplenishmentNoteId(@Param("replenishmentNoteId") String replenishmentNoteId);
	
	/**
	 * 根据id获取数据
	 * @param id
	 * @return
	 */
	ReplenishmentNoteDetail getDetailById(@Param("id") String id);
	
	/**
	 * 添加数据
	 * @param datail
	 */
	void addDetail(@Param("detail") ReplenishmentNoteDetail datail);
	
	/**
	 * 修改数据
	 * @param datail
	 * @return
	 */
	int updateDetail(@Param("detail") ReplenishmentNoteDetail datail);
	
	/**
	 * 根据换货单id删除数据
	 * @param replenishmentNoteId
	 * @return
	 */
	int deleteDetailByReplenishmentNoteId(@Param("replenishmentNoteId") String replenishmentNoteId);
	
	/**
	 * 批量删除数据
	 * @param ids
	 * @return
	 */
	int deleteDetailById(@Param("ids") String[] ids);
	
	int updateNumberById(@Param("id") String id, @Param("number") BigDecimal number, @Param("CostPrice") BigDecimal costPrice, @Param("totalMoney") BigDecimal totalMoney);
}
