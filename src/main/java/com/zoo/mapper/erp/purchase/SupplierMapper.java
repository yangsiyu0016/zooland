package com.zoo.mapper.erp.purchase;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.purchase.Supplier;

@Component
public interface SupplierMapper {
	
	//分页查询
	List<Supplier> getSupplierByPage(@Param("start")Integer start, @Param("size")Integer size, @Param("companyId")String companyId);

	//获取总条数
	Long getCount();

	//新增供货商
	void add(@Param("supplier") Supplier supplier);
	
	//修改供货商信息
	void update(@Param("supplier") Supplier supplier);
}
