package com.zoo.mapper.crm.customer;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.crm.Receiving;

@Component
public interface ReceivingMapper {
	
	int addReceiving(@Param("receiving")Receiving receiving);

	List<Receiving> getReceivingByPage(@Param("start")Integer start, @Param("size")Integer size, @Param("customerId")String customerId);
	List<Receiving> getReceivingByCustomerId(@Param("customerId") String customerId);
	long getCount(@Param("customerId")String customerId);

	int updateReceiving(@Param("receiving")Receiving receiving);

}
