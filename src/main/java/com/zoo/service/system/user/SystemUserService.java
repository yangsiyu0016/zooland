package com.zoo.service.system.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.system.company.CompanyMapper;
import com.zoo.mapper.system.menu.SystemMenuMapper;
import com.zoo.mapper.system.user.SystemUserMapper;
import com.zoo.model.system.company.Company;
import com.zoo.model.system.menu.SystemMenu;
import com.zoo.model.system.position.Position;
import com.zoo.model.system.user.SystemUser;
import com.zoo.model.system.user.UserInfo;
import com.zoo.properties.JwtProperties;
import com.zoo.utils.CodecUtils;
import com.zoo.utils.JwtUtils;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@Transactional
public class SystemUserService {
	@Autowired
	SystemUserMapper systemUserMapper;
	@Autowired
	CompanyMapper companyMapper;
	@Autowired
	SystemMenuMapper menuMapper;
	@Autowired
    private JwtProperties props;
	public Map<String,Object> login(String username,String password) {
		try {
			
			Map<String,Object> resultMap = new HashMap<String,Object>();
			SystemUser user = this.queryUser(username, password);
			if(user==null) {
				//return null;
				log.error(ExceptionEnum.USERNAME_OR_PASSWORD_ERROR.message());
				throw new ZooException(ExceptionEnum.USERNAME_OR_PASSWORD_ERROR);
			}
			resultMap.put("user", user);
			UserInfo userInfo = new UserInfo(user.getId(), user.getUserName(),user.getCompanyId());
			//生成Token
			String token = JwtUtils.generateToken(userInfo, props.getPrivateKey(), props.getExpire());
			resultMap.put("token", token);
			
			List<String> allowPath = new ArrayList<String>();
			if("ADMIN".equals(user.getType())) {
				List<SystemMenu> menuList =  menuMapper.getAdminMenu();
				
				for(SystemMenu menu:menuList) {
					allowPath.add(menu.getPath());
				}
				
			}else if("MANAGER".equals(user.getType())) {
				Company company = companyMapper.getCompanyById(user.getCompanyId());
				List<SystemMenu> menuList = menuMapper.getMenuByCompanyTypeId(company.getCompanyType().getId());
				for(SystemMenu menu:menuList) {
					System.out.println(menu);
					allowPath.add(menu.getPath());
				}
			}else {
				List<Position> positions = user.getPositions();
				for(Position position:positions) {
					List<SystemMenu> menuList = menuMapper.getMenuByPositionId(position.getId());
					for(SystemMenu menu:menuList) {
						if(!allowPath.contains(menu.getPath())) allowPath.add(menu.getPath());
					}
				}
			}
			resultMap.put("allowPath", allowPath);
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("【授权中心】用户名和密码错误，用户名：{}", username,e);
			throw new ZooException(ExceptionEnum.CREATE_TOKEN_ERROR);
		}
	}
	public SystemUser queryUser(String username,String password) {
		SystemUser record = new SystemUser();
		record.setUserName(username);
		SystemUser user = systemUserMapper.getUserByUserName(username);
		if(user==null) {
			throw new ZooException(ExceptionEnum.USER_NOT_EXIST);
		}
		if(!user.getPassword().equals(CodecUtils.md5Hex(password, user.getSalt()))) {
			//密码不正确
            throw new ZooException(ExceptionEnum.PASSWORD_NOT_MATCHING);
		}
		return user;
	}
	public List<SystemUser> queryUserByPage(Integer page,Integer size,String keywords){
		int start = (page-1)*size;
		UserInfo user = LoginInterceptor.getLoginUser();
		List<SystemUser> users = systemUserMapper.getUserByPage(start, size,keywords,user.getCompanyId());
		return users;
	}
	public Long getCountByKeywords(String keywords) {
		UserInfo user = LoginInterceptor.getLoginUser();
		return systemUserMapper.getCountByKeywords(keywords,user.getCompanyId());
	}
	public void add(SystemUser user) {
		if(StringUtils.isBlank(user.getId())) user.setId(UUID.randomUUID().toString());
		user.setType("NORMAL");
	    if(StringUtils.isBlank(user.getPassword())) {
	    	user.setPassword("123456");
	    }
		
	    if(StringUtils.isBlank(user.getCompanyId())) { 
			  UserInfo uinfo = LoginInterceptor.getLoginUser(); 
			  user.setCompanyId(uinfo.getCompanyId()); 
		}
		 
	    String salt =  CodecUtils.generateSalt();
	    user.setSalt(salt);
	    user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));
	    systemUserMapper.addUser(user);
	    if(user.getPositions()!=null) {
	    	for(Position position:user.getPositions()) {
		    	systemUserMapper.addPosition(UUID.randomUUID().toString(),user.getId(),position.getId());
		    }
	    }
	    
	}
	public List<SystemUser> getAllUsers(){
		UserInfo uinfo = LoginInterceptor.getLoginUser(); 
		return systemUserMapper.getUserByPage(null, null, "",uinfo.getCompanyId());
	}
	public int updateUser(SystemUser user) {
		systemUserMapper.deletePosition(user.getId());
		for(Position position:user.getPositions()) {
	    	systemUserMapper.addPosition(UUID.randomUUID().toString(),user.getId(),position.getId());
	    }
		return systemUserMapper.updateUser(user);
	}
	public SystemUser getUserById(String id) {
		// TODO Auto-generated method stub
		return systemUserMapper.getUserById(id);
	}
}
