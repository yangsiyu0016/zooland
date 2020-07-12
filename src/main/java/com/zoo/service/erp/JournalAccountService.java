package com.zoo.service.erp;


import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.JournalAccountMapper;
import com.zoo.model.erp.JournalAccount;
import com.zoo.model.erp.product.Product;
import com.zoo.model.erp.warehouse.Stock;

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
	public List<JournalAccount> getAccountsOfExport(String keywords,String code,String productCode,String productName,String warehouseId,String start_ctime,String end_ctime,String sort,String order) {
		//获取要导出的库存变动明细
		List<JournalAccount> accounts = jaMapper.getJournalAccountByPage(null, null, keywords, code, productCode, productName, warehouseId, start_ctime, end_ctime, LoginInterceptor.getLoginUser().getCompanyId(), sort, order);
		JournalAccount account = new JournalAccount();
		account.setType("HJ");
		BigDecimal inboundNumber = new BigDecimal("0");
		BigDecimal inboundTotalMoney = new BigDecimal("0");
		BigDecimal outboundNumber = new BigDecimal("0");
		BigDecimal outboundTotalMoney = new BigDecimal("0");
		for(JournalAccount journalAccount: accounts) {
			inboundNumber = inboundNumber.add(journalAccount.getRkNumber() == null? new BigDecimal("0") : journalAccount.getRkNumber());
			inboundTotalMoney = inboundTotalMoney.add(journalAccount.getRkTotalMoney() == null? new BigDecimal("0") : journalAccount.getRkTotalMoney());
			outboundNumber = outboundNumber.add(journalAccount.getCkNumber() == null? new BigDecimal("0") : journalAccount.getCkNumber());
			outboundTotalMoney = outboundTotalMoney.add(journalAccount.getCkTotalMoney() == null? new BigDecimal("0") : journalAccount.getCkTotalMoney());
		}
		account.setRkNumber(inboundNumber);
		account.setRkTotalMoney(inboundTotalMoney);
		account.setCkNumber(outboundNumber);
		account.setCkTotalMoney(outboundTotalMoney);
		account.setTotalNumber(inboundNumber.subtract(outboundNumber));
		account.setTotalMoney(inboundTotalMoney.subtract(outboundTotalMoney));
		Product product = new Product();
		Stock stock = new Stock();
		stock.setProduct(product);
		account.setStock(stock);
		accounts.add(account);
		return accounts;
	}
}
