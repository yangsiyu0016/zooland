package com.zoo.mapper.erp.sell;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.sell.SellDetail;

@Component
public interface SellDetailMapper {

	int addDetail(@Param("detail")SellDetail detail);

	int deleteDetailBySellId(@Param("sellId")String sellId);

	List<SellDetail> getDetailBySellId(@Param("sellId")String sellId);
	List<SellDetail> getNotOutDetailBySellId(@Param("sellId")String sellId);

	int updateDetail(@Param("detail")SellDetail detail);

	int deleteDetailById(@Param("ids")String[] ids);
	

}
