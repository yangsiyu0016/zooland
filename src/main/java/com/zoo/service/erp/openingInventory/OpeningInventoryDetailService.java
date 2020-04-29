package com.zoo.service.erp.openingInventory;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.erp.openingInventory.OpeningInventoryDetailMapper;
import com.zoo.model.erp.openingInventory.OpeningInventoryDetail;

@Service
@Transactional
public class OpeningInventoryDetailService {
	@Autowired
	OpeningInventoryDetailMapper detailMapper;
	public List<OpeningInventoryDetail> getDetailsByOpeningInventoryId(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	public int updatePrice(String id, String costPrice,String totalMoney) {
		return detailMapper.updatePrice(id,new BigDecimal(costPrice),new BigDecimal(totalMoney));
		
	}

}
