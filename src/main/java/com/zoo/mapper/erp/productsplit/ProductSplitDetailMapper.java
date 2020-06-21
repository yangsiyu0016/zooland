package com.zoo.mapper.erp.productsplit;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.productsplit.ProductSplitDetail;

@Component
public interface ProductSplitDetailMapper {
	
	//添加
	void addDetail(@Param("productSplitDetail") ProductSplitDetail detail);
	//更新
	int updateDetail(@Param("productSplitDetail") ProductSplitDetail detail);
	//批量删除
	int deleteDetailById(@Param("ids") String[] ids);
	//更新未入库数
	int updateNumber(@Param("id") String id, @Param("notInNumber") BigDecimal notInNumber);
}
