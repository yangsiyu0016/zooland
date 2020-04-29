package com.zoo.service.system.company;


import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoo.mapper.system.company.CompanyMapper;
import com.zoo.model.system.company.Company;
import com.zoo.model.system.user.SystemUser;
import com.zoo.service.system.user.SystemUserService;

@Service
@Transactional
public class CompanyService {
	
	@Autowired
	SystemUserService systemUserService;
	@Autowired
	CompanyMapper companyMapper;
	
	public List<Company> getCompanyByPage(Integer page,Integer size){
		int start = (page-1)*size;
		return companyMapper.getCompanyByPage(start, size);
	}
	public long getCount() {
		return companyMapper.getCount();
	}
	public List<Company> getAllCmpany(){
		return companyMapper.getCompanyByPage(null,null);
	}
	public Company getCompanyById(String id) {
		return companyMapper.getCompanyById(id);
	}
	public void addCompany(Company company) {


	      String companyId = UUID.randomUUID().toString();
	      String userId = UUID.randomUUID().toString();
	      company.setId(companyId);
	      company.setManagerId(userId);
	      companyMapper.addCompany(company);
	   
	      SystemUser user = new SystemUser();
	      user.setId(userId);
	      user.setRealName("系统管理员");
	      user.setUserName(company.getUserName());
	      user.setPassword("123456");
	      user.setCompanyId(companyId);
	      
	      systemUserService.add(user);
	}
}
