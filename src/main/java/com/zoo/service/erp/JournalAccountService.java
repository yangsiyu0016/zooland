package com.zoo.service.erp;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.JournalAccountMapper;
import com.zoo.model.erp.JournalAccount;

@Service
@Transactional
public class JournalAccountService {
	@Autowired
	JournalAccountMapper jaMapper;
	public List<JournalAccount> getJournalAccountByPage(Integer page,Integer size){
		Integer start = (page-1)*size;
		List<JournalAccount> list = jaMapper.getJournalAccountByPage(start, size, LoginInterceptor.getLoginUser().getCompanyId());
		return list;
	}
	public long getCount() {
		return jaMapper.getCount(LoginInterceptor.getLoginUser().getCompanyId());
	}
	public int addJournalAccount(JournalAccount journalAccount) {
		return jaMapper.addJournalAccount(journalAccount);		
	}
	public int deleteByOrderDetailId(String orderDetailId) {
		return jaMapper.deleteByOrderDetailId(orderDetailId);
		
	}

}
