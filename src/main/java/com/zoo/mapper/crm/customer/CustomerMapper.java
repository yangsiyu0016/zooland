package com.zoo.mapper.crm.customer;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.crm.Customer;

@Component
public interface CustomerMapper {
	List<Customer> getCustomerByPage(@Param("start")Integer start,@Param("size")Integer size,@Param("companyId")String companyId);
	long getCount(@Param("companyId")String companyId);
	int addCustomer(@Param("customer")Customer customer);
	int updateCustomer(@Param("customer")Customer customer);

}
