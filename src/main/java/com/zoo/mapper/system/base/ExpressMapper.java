package com.zoo.mapper.system.base;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.system.base.Express;

@Component
public interface ExpressMapper {
	List<Express> getExpressByPage(@Param("start")Integer start,@Param("size")Integer size);
	long getCount();
	int addExpress(@Param("express")Express express);
	int updateExpress(@Param("express")Express express);
}
