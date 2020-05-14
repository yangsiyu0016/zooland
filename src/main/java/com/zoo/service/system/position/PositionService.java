package com.zoo.service.system.position;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.system.position.PositionMapper;
import com.zoo.model.system.position.Position;
import com.zoo.model.system.user.UserInfo;

@Service
@Transactional
public class PositionService {
	@Autowired
	PositionMapper positionMapper;
	public List<Position> getPositionByPage(Integer page,Integer size){
		int start = (page-1)*size;
		UserInfo user = LoginInterceptor.getLoginUser();
		return positionMapper.getPositionByPage(start, size,user.getCompanyId());
	}
	public Long getCount() {
		UserInfo user = LoginInterceptor.getLoginUser();
		return positionMapper.getCount(user.getCompanyId());
	}
	public void addPosition(Position position) {
		position.setId(UUID.randomUUID().toString());
		UserInfo user = LoginInterceptor.getLoginUser();
		
		position.setCompanyId(user.getCompanyId());
		positionMapper.addPosition(position);
	}
	public List<Position> getAllPositons() {
		UserInfo uinfo = LoginInterceptor.getLoginUser();
		return positionMapper.getPositionByPage(null, null,uinfo.getCompanyId());
	}
	public List<Position> getPositionByCondition(Map<String, Object> condition) {
		// TODO Auto-generated method stub
		return positionMapper.getPositionByCondition(condition);
	}
	public void updatePosition(Position position) {
		positionMapper.updatePosition(position);
		
	}
}
