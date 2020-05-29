package com.zoo.mapper.flow;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.flow.HistoryAction;

@Component
public interface HistoryActionMapper {
	List<HistoryAction> getHistoryActionByProcessInstanceId(@Param("processInstanceId") String processIntanceId);
}
