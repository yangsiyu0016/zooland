package com.zoo.mapper.system.base;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.system.base.Express;

@Component
public interface ExpressMapper {
	List<Express> getExpressByPage(@Param("start")Integer start,@Param("size")Integer size, @Param("keywords") String keywords, @Param("expressName")String expressName, @Param("expressType")String expressType, @Param("startAddress")String startAddress, @Param("companyId") String companyId);
	long getCount(@Param("keywords") String keywords, @Param("expressName")String expressName, @Param("expressType")String expressType, @Param("startAddress")String startAddress, @Param("companyId") String companyId);
	int addExpress(@Param("express")Express express);
	int updateExpress(@Param("express")Express express);
	Express getExpressById(@Param("id")String id);
	/**
	 * 根据名称获取条数
	 * @param expressName
	 * @param companyId
	 * @return
	 */
	long selectCountByExpressName(@Param("expressName") String expressName, @Param("companyId") String companyId);
	/**
	 * 删除
	 * @param split
	 */
	void deleteById(@Param("ids") String[] ids);
	/**
	 * 根据要修改的物流id获取不重复物流信息数量
	 * @param id
	 * @return
	 */
	long getNoRepeatCountByEditExpressName(@Param("id") String id, @Param("name") String name, @Param("companyId") String companyId);
}
