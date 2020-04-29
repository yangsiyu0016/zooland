package com.zoo.service.crm.customer;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.crm.customer.ReceivingMapper;
import com.zoo.model.crm.Receiving;

@Service
@Transactional
public class ReceivingService {
	@Autowired
	ReceivingMapper receivingMapper;
	public List<Receiving> getReceivingByPage(Integer page, Integer size, String customerId) {
		
		Integer start = null; 
		if(page!=null) {
			start = (page-1)*size;
		}
		
		List<Receiving> receivings = receivingMapper.getReceivingByPage(start,size,customerId);
		return receivings;
	}
	public long getCount(String customerId) {
		// TODO Auto-generated method stub
		return receivingMapper.getCount(customerId);
	}
	public List<Receiving> getReceivingByCustomerId(String customerId){
		
		List<Receiving> receivings = this.getReceivingByPage(null, null, customerId);
		for(Receiving receiving:receivings) {
			String receivingContext = receiving.getProvince().getName()+"/"+receiving.getCity().getName()+"/"+receiving.getCounty().getName()+"/"
					+receiving.getAddress()+","+receiving.getConsignee()+","+receiving.getCellphone();
			receiving.setReceivingContext(receivingContext);
		}
		return receivings;
	}
	public Receiving  addReceiving(Receiving receiving) {
		receiving.setId(UUID.randomUUID().toString());
		List<String> receivingarea = receiving.getArea();
		receiving.setCountryId(receivingarea.get(0));
		receiving.setProvinceId(receivingarea.get(1));
		receiving.setCityId(receivingarea.get(2));
		receiving.setCountyId(receivingarea.get(3));
		receivingMapper.addReceiving(receiving);
		return receiving;
	}
	public int updateReceiving(Receiving receiving) {
		receiving.setId(UUID.randomUUID().toString());
		List<String> receivingarea = receiving.getArea();
		receiving.setCountryId(receivingarea.get(0));
		receiving.setProvinceId(receivingarea.get(1));
		receiving.setCityId(receivingarea.get(2));
		receiving.setCountyId(receivingarea.get(3));
		return receivingMapper.updateReceiving(receiving);
		
	}
	
}
