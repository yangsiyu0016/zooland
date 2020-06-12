package com.zoo.mapper.system.parameter;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.system.parameter.SystemParameterDirectory;

@Component
public interface SystemParameterDirectoryMapper {
	List<SystemParameterDirectory> getTreeData(@Param("companyId")String companyId);
	List<SystemParameterDirectory> getDirectoryList(@Param("companyId")String companyId);
	int addDirectory(@Param("directory")SystemParameterDirectory directory);
	int update(@Param("directory") SystemParameterDirectory directory);
	int delete(@Param("id") String id);
}
