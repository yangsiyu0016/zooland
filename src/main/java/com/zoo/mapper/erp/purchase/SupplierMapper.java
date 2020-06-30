package com.zoo.mapper.erp.purchase;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.purchase.Supplier;

@Component
public interface SupplierMapper {
	
	//分页查询
	List<Supplier> getSupplierByPage(
			@Param("start")Integer start,
			@Param("size")Integer size,
			@Param("companyId")String companyId,
			@Param("keywords")String keywords,
			@Param("supplierName")String supplierName,
			@Param("linkman")String linkman,
			@Param("owner")String owner,
			@Param("start_gtime")String start_gtime,
			@Param("end_gtime")String end_gtime,
			@Param("start_createTime")String start_createTime,
			@Param("end_createTime")String end_createTime,
			@Param("sort")String sort,
			@Param("order")String order);

	//获取总条数
	Long getCount(
			@Param("keywords")String keywords,
			@Param("supplierName")String supplierName,
			@Param("linkman")String linkman,
			@Param("owner")String owner,
			@Param("start_gtime")String start_gtime,
			@Param("end_gtime")String end_gtime,
			@Param("start_createTime")String start_createTime,
			@Param("end_createTime")String end_createTime,
			@Param("companyId")String companyId);

	//新增供货商
	void add(@Param("supplier") Supplier supplier);
	
	//修改供货商信息
	void update(@Param("supplier") Supplier supplier);
}
