package com.zoo.mapper.crm.customer;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.crm.Customer;

@Component
public interface CustomerMapper {
	List<Customer> getCustomerByPage(
			@Param("start")Integer start,
			@Param("size")Integer size,
			@Param("companyId")String companyId,
			@Param("keywords")String keywords,
			@Param("customerName")String customerName,
			@Param("linkman")String linkman,
			@Param("owner")String owner,
			@Param("start_gtime")String start_gtime,
			@Param("end_gtime")String end_gtime,
			@Param("start_created")String start_created,
			@Param("end_created")String end_created,
			@Param("sort")String sort,
			@Param("order")String order);
	long getCount(
			@Param("keywords")String keywords,
			@Param("customerName")String customerName,
			@Param("linkman")String linkman,
			@Param("owner")String owner,
			@Param("start_gtime")String start_gtime,
			@Param("end_gtime")String end_gtime,
			@Param("start_created")String start_created,
			@Param("end_created")String end_created,
			@Param("companyId")String companyId);
	int addCustomer(@Param("customer")Customer customer);
	int updateCustomer(@Param("customer")Customer customer);

}
