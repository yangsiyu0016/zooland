package com.zoo.service.flow;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.flow.HistoryActionMapper;
import com.zoo.model.flow.HistoryAction;

@Service
@Transactional
public class HitoryActionService {
	@Autowired
	HistoryActionMapper haMapper;
	public List<HistoryAction> getHistoryActionByProcessInstanceId(String processInstanceId){
		return haMapper.getHistoryActionByProcessInstanceId(processInstanceId);
	}
}
