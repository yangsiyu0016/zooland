package com.zoo.service.system.base;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.system.base.ExpressMapper;
import com.zoo.model.system.base.Express;

@Service
@Transactional
public class ExpressService {
	@Autowired
	ExpressMapper expressMapper;
	public List<Express> getExpressByPage(Integer page, Integer size, String keywords, String expressName, String expressType, String startAddress){
		
		Integer start = null;
		if(page!=null) {
			start = (page-1)*size;
		}
		List<Express> expresses = expressMapper.getExpressByPage(start, size, keywords, expressName, expressType, startAddress, LoginInterceptor.getLoginUser().getCompanyId());
		return expresses;
	}
	public long getCount(String keywords, String expressName, String expressType, String startAddress) {
		return expressMapper.getCount(keywords, expressName, expressType, startAddress, LoginInterceptor.getLoginUser().getCompanyId());
	}
	public void addExpress(Express express) {
		express.setId(UUID.randomUUID().toString());
		express.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		express.setCtime(new Date());
		long count = expressMapper.selectCountByExpressName(express.getName(), LoginInterceptor.getLoginUser().getCompanyId());
		if(count>0) {//如果有重名，不能添加
			throw new ZooException(ExceptionEnum.EXPRESS_NAME_REPEAT);
		}else {//如果没有重名，添加物流信息
			expressMapper.addExpress(express);
		}
	}
	public void updateExpress(Express express) {
		long num = expressMapper.getNoRepeatCountByEditExpressName(express.getId(), express.getName(), LoginInterceptor.getLoginUser().getCompanyId());
		if(num > 0) {//说明有重复物流信息
			throw new ZooException(ExceptionEnum.EXPRESS_NAME_REPEAT);
		}else {//如果没有重名，添加物流信息
			expressMapper.updateExpress(express);
		}
	}
	public Express getExpressById(String id) {
		// TODO Auto-generated method stub
		return expressMapper.getExpressById(id);
	}
	//删除
	public void deleteById(String ids) {
		// TODO Auto-generated method stub
		String[] split = ids.split(",");
		expressMapper.deleteById(split);
	}
}
