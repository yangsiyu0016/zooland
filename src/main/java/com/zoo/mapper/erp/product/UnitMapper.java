package com.zoo.mapper.erp.product;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import com.zoo.model.erp.product.Unit;

@Component
public interface UnitMapper {
	List<Unit> getUnitList();
	int addUnit(@Param("unit")Unit unit);
	/**
	 * 分页获取
	 * @param start
	 * @param page
	 * @param keywords
	 * @return
	 */
	List<Unit> getUnitByPage(@Param("start")Integer start, @Param("size")Integer size, @Param("keywords")String keywords);
	/**
	 * 总数
	 * @param keywords
	 * @return
	 */
	long getCount(@Param("keywords") String keywords);
	/**
	 * 修改
	 * @param unit
	 */
	void update(@Param("unit")Unit unit);
	
	Unit getUnitById(@Param("id") String id);
	/**
	 * 删除
	 * @param ids
	 */
	void delete(@Param("ids")String[] ids);
	
	Long getCountByRepeat(@Param("name") String name);
	
	Long getCountByRepeatOfEdit(@Param("name") String name, @Param("id") String id);
}
