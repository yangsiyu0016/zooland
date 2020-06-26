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
	public List<JournalAccount> getJournalAccountByPage(Integer page,Integer size,String keywords,String code,String productCode,String productName,String warehouseId,String start_ctime,String end_ctime,String sort,String order){
		Integer start = (page-1)*size;
		List<JournalAccount> list = jaMapper.getJournalAccountByPage(start, size,keywords,code,productCode,productName,warehouseId,start_ctime,end_ctime, LoginInterceptor.getLoginUser().getCompanyId(),sort,order);
		return list;
	}
	public long getCount(String keywords,String code,String productCode,String productName,String warehouseId) {
		return jaMapper.getCount(keywords,code,productCode,productName,warehouseId,LoginInterceptor.getLoginUser().getCompanyId());
	}
	public int addJournalAccount(JournalAccount journalAccount) {
		return jaMapper.addJournalAccount(journalAccount);		
	}
	public int deleteByOrderDetailId(String orderDetailId) {
		return jaMapper.deleteByOrderDetailId(orderDetailId);
		
	}

}
