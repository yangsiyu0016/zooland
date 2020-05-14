package com.zoo.mapper.system.position;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.system.position.Position;
@Component
public interface PositionMapper {
	List<Position> getPositionByPage(@Param("start") Integer start,@Param("size") Integer size,@Param("companyId")String companyId);
	Long getCount(@Param("companyId")String companyId);
	int addPosition(@Param("position") Position position);
	List<Position> getPositionByCondition(@Param("condition")Map<String, Object> condition);
	int updatePosition(@Param("position")Position position);
}
