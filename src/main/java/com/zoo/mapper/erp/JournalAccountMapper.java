package com.zoo.mapper.erp;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.erp.JournalAccount;

@Component
public interface JournalAccountMapper {
	List<JournalAccount> getJournalAccountByPage(@Param("start")Integer start,@Param("size")Integer size,@Param("companyId") String companyId);
	long getCount(@Param("companyId")String companyId);
	int addJournalAccount(@Param("ja")JournalAccount ja);

}
