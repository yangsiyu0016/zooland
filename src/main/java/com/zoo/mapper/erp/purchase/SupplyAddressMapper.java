package com.zoo.mapper.erp.purchase;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.purchase.SupplyAddress;

@Component
public interface SupplyAddressMapper {
	
	//根据id获取供货地址
	public List<SupplyAddress> getAddressById(@Param("id") String id);
	
	//新增数据
	public void add(@Param("supplyAddress") SupplyAddress supplyAddress);
	
	//修改数据
	public int update(@Param("supplyAddress") SupplyAddress supplyAddress);
}
