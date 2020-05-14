package com.zoo.mapper.system.company;



import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zoo.model.system.company.CompanyType;

public interface CompanyTypeMapper {
	List<CompanyType> getCompanyTypeByPage(@Param("start") Integer start,@Param("size") Integer size);
	Long getCount();
	List<String> getResourceByTypeId(@Param("typeId")String typeId);
	int addCompanyType(@Param("type") CompanyType type);
	int addResource(@Param("id") String id,@Param("companyTypeId") String companyTypeId,@Param("menuId") String menuId);
	int deleteResourceByTypeId(@Param("typeId") String typeId);
	Long getCountByCompanyIdAndMenuId(@Param("typeId") String typeId,@Param("menuId") String menuId);
	int updateCompanyType(@Param("type")CompanyType type);
}
