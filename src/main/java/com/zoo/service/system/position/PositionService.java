package com.zoo.service.system.position;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.system.menu.SystemMenuMapper;
import com.zoo.mapper.system.position.PositionMapper;
import com.zoo.model.system.menu.SystemMenu;
import com.zoo.model.system.position.Position;
import com.zoo.model.system.user.UserInfo;

@Service
@Transactional
public class PositionService {
	@Autowired
	PositionMapper positionMapper;
	@Autowired
	SystemMenuMapper menuMapper;
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
	public List<String> getResource(String positionId) {
		return positionMapper.getResourceByPositionId(positionId);
		
	}
	public void updateResource(String positionId, String[] menuIds) {
		positionMapper.deleteResourceByPositionId(positionId);
		for(String menuId:menuIds) {
			String id = UUID.randomUUID().toString();
			SystemMenu menu =menuMapper.getMenuById(menuId);
			positionMapper.addResource(id, positionId, menuId);
			if(!StringUtils.isBlank(menu.getParentId())) {
				addParentMenu(positionId,menu.getParentId());
			}
		}
		
	}
	private void addParentMenu(String positionId, String parentId) {
		long count = positionMapper.getCountByPositionIdAndMenuId(positionId, parentId);
		if(count==0) {
			positionMapper.addResource(UUID.randomUUID().toString(), positionId, parentId);
			SystemMenu menu = menuMapper.getMenuById(parentId);
			if(!StringUtils.isBlank(menu.getParentId())) {
				addParentMenu(positionId,menu.getParentId());
			}
		}
		
		
	}
}
