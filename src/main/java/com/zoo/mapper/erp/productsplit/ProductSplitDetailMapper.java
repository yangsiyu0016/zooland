package com.zoo.mapper.erp.productsplit;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.productsplit.ProductSplitDetail;

@Component
public interface ProductSplitDetailMapper {
	
	//添加
	void addDetail(@Param("detail") ProductSplitDetail detail);
	//更新
	int updateDetail(@Param("detail") ProductSplitDetail detail);
	//批量删除
	int deleteDetailById(@Param("ids") String[] ids);
	//更新未入库数
	int updateNumber(@Param("id") String id, @Param("notInNumber") BigDecimal notInNumber);
	
	//根据拆分单删除
	int deleteByProductSplitId(@Param("productSplitId") String productSplitId);
	
	ProductSplitDetail getDetailById(@Param("id") String id);
}
