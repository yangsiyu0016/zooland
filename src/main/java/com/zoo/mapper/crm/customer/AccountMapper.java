package com.zoo.mapper.crm.customer;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.crm.Account;

@Component
public interface AccountMapper {

	public int addAccount(@Param("account")Account account);

}
