package com.zoo.mapper.system.company;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.system.company.Company;

@Component
public interface CompanyMapper {
	List<Company> getCompanyByPage(@Param("start") Integer start,@Param("size") Integer size);
	Long getCount();
	Company getCompanyById(@Param("id") String id);
	int deleteCompanyById(@Param("id")String id);
	int addCompany(@Param("company") Company company);
}
