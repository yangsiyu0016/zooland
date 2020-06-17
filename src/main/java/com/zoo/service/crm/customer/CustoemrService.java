package com.zoo.service.crm.customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.crm.customer.AccountMapper;
import com.zoo.mapper.crm.customer.CustomerMapper;
import com.zoo.mapper.crm.customer.LinkmanMapper;
import com.zoo.mapper.crm.customer.ReceivingMapper;
import com.zoo.mapper.system.area.AreaMapper;
import com.zoo.model.crm.Customer;
import com.zoo.model.crm.Linkman;
import com.zoo.model.crm.Receiving;

@Service
@Transactional
public class CustoemrService {
	@Autowired
	CustomerMapper customerMapper;
	@Autowired
	AccountMapper accountMapper;
	@Autowired
	LinkmanMapper linkmanMapper;
	@Autowired
	AreaMapper areaMapper;
	@Autowired
	ReceivingMapper receivingMapper;
	public void addCustomer(Customer customer) {
		customer.setId(UUID.randomUUID().toString());
		customer.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		List<String> area = customer.getArea();
		if(area.size()>0) {
			customer.setCountryId(area.get(0));
			customer.setProvinceId(area.get(1));
			customer.setCityId(area.get(2));
			customer.setCountyId(area.get(3));
		}
		
		customer.setCreated(new Date());
		customer.setType("E");
		customer.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		customer.setSystemUserId(LoginInterceptor.getLoginUser().getId());
		customerMapper.addCustomer(customer);
		
		/*
		 * List<Account> accounts = customer.getAccounts(); for(Account
		 * account:accounts) { account.setId(UUID.randomUUID().toString());
		 * account.setCustomerId(customer.getId()); accountMapper.addAccount(account); }
		 */
		
		List<Linkman> linkmans = customer.getLinkmans();
		for(Linkman linkman:linkmans) {
			linkman.setId(UUID.randomUUID().toString());
			linkman.setCustomerId(customer.getId());
			linkmanMapper.addLinkman(linkman);
		}
		
		List<Receiving> receivings = customer.getReceivings();
		for(Receiving receiving:receivings){
			receiving.setId(UUID.randomUUID().toString());
			receiving.setCustomerId(customer.getId());
			List<String> receivingarea = receiving.getArea();
			receiving.setCountryId(receivingarea.get(0));
			receiving.setProvinceId(receivingarea.get(1));
			receiving.setCityId(receivingarea.get(2));
			receiving.setCountyId(receivingarea.get(3));
			receivingMapper.addReceiving(receiving);
		}
	}
	public void updateCustomer(Customer customer) {
		List<String> area = customer.getArea();
		if(area.size()>0) {
			customer.setCountryId(area.get(0));
			customer.setProvinceId(area.get(1));
			customer.setCityId(area.get(2));
			customer.setCountyId(area.get(3));
		}else {
			customer.setCountryId(null);
			customer.setProvinceId(null);
			customer.setCityId(null);
			customer.setCountyId(null);
		}
		customerMapper.updateCustomer(customer);
	}
	public List<Customer> getCustomerByPage(Integer page, Integer size) {
		Integer start = (page-1)*size;
		List<Customer> customers = customerMapper.getCustomerByPage(start, size, LoginInterceptor.getLoginUser().getCompanyId());
		
		for(Customer customer: customers) {
			if(customer.getCountry()!=null) {
				List<String> area = new ArrayList<String>();
				
				area.add(customer.getCountryId());
				area.add(customer.getProvinceId());
				area.add(customer.getCityId());
				area.add(customer.getCountyId());
				
				
				
				customer.setArea(area);
				customer.setAreaPath(customer.getCountry().getName()+"/"+customer.getProvince().getName()+"/"+customer.getCity().getName()+"/"+customer.getCounty().getName());
				
			}
			
			
			List<Receiving> receivings = customer.getReceivings();
			for(Receiving receiving:receivings) {
				List<String> areas = new ArrayList<String>();
				areas.add(receiving.getCountry().getId());
				areas.add(receiving.getProvince().getId());
				areas.add(receiving.getCity().getId());
				areas.add(receiving.getCounty().getId());
				String receivingContext = receiving.getProvince().getName()+"/"+receiving.getCity().getName()+"/"+receiving.getCounty().getName()+"/"
						+receiving.getAddress()+","+receiving.getConsignee()+","+receiving.getCellphone();
				receiving.setArea(areas);
				receiving.setReceivingContext(receivingContext);
			}
			customer.setReceivings(receivings);
			
		}
		return customers;
	}
	public long getCount() {
		return customerMapper.getCount(LoginInterceptor.getLoginUser().getCompanyId());
	}
}
