package com.zoo.service.system.base;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.system.base.ExpressMapper;
import com.zoo.model.system.base.Express;

@Service
@Transactional
public class ExpressService {
	@Autowired
	ExpressMapper expressMapper;
	public List<Express> getExpressByPage(Integer page,Integer size){
		
		Integer start = null;
		if(page!=null) {
			start = (page-1)*size;
		}
		List<Express> expresses = expressMapper.getExpressByPage(start, size);
		return expresses;
	}
	public long getCount() {
		return expressMapper.getCount();
	}
	public void addExpress(Express express) {
		express.setId(UUID.randomUUID().toString());
		express.setCtime(new Date());
		expressMapper.addExpress(express);
	}
	public void updateExpress(Express express) {
		expressMapper.updateExpress(express);
		
	}
	public Express getExpressById(String id) {
		// TODO Auto-generated method stub
		return expressMapper.getExpressById(id);
	}
}
