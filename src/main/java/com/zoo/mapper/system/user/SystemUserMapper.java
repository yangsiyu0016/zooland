package com.zoo.mapper.system.user;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.system.user.SystemUser;


@Component
public interface SystemUserMapper {
	List<SystemUser> getUserByPage(
			@Param("start") Integer start,
			@Param("size") Integer size,
			@Param("keywords") String keywords,
			@Param("companyId") String companyId);
	Long getCountByKeywords(@Param("keywords")String keywords,@Param("companyId") String companyId);
	SystemUser getUserByUserName(@Param("username")String username);
	int addUser(@Param("user")SystemUser user);
	int updateUser(@Param("user") SystemUser user);
	Long getCountByParentId(@Param("parentId") String parentId);
	
	int addTry(@Param("tx_no")String tx_no);
	int addConfirm(@Param("tx_no")String tx_no);
	int addCancel(@Param("tx_no")String tx_no);
	
	int isExistTry(@Param("tx_no") String tx_no);
	int isExistConfirm(@Param("tx_no") String tx_no);
	int isExistCancel(@Param("tx_no") String tx_no);
	int addPosition(@Param("id")String id, @Param("userId")String userId, @Param("positionId")String positionId);
	int deletePosition(@Param("userId")String userId);
	SystemUser getUserById(@Param("id")String id);
}
