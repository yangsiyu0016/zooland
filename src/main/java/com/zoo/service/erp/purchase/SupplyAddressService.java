package com.zoo.service.erp.purchase;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.erp.purchase.SupplyAddressMapper;
import com.zoo.model.erp.purchase.SupplyAddress;

@Service
@Transactional
public class SupplyAddressService {

	@Autowired
	private SupplyAddressMapper supplyAddressMapper;

	public SupplyAddress addSupplyAddress(SupplyAddress supplyAddress) {
		// TODO Auto-generated method stub
		supplyAddress.setId(UUID.randomUUID().toString());
		supplyAddressMapper.add(supplyAddress);
		return supplyAddress;
	}

	public int updateSupplyAddress(SupplyAddress supplyAddress) {
		// TODO Auto-generated method stub
		return supplyAddressMapper.update(supplyAddress);
		
	}
	
}
