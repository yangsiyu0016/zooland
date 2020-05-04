package com.zoo.service.erp.purchase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.purchase.ContactMapper;
import com.zoo.mapper.erp.purchase.SupplierMapper;
import com.zoo.mapper.erp.purchase.SupplyAddressMapper;
import com.zoo.model.erp.purchase.Contact;
import com.zoo.model.erp.purchase.Purchase;
import com.zoo.model.erp.purchase.Supplier;
import com.zoo.model.erp.purchase.SupplyAddress;
import com.zoo.model.system.user.SystemUser;

@Service
@Transactional
public class SupplierService {

	@Autowired
	private SupplierMapper supplierMapper;
	
	//供货地址mapper
	@Autowired
	private SupplyAddressMapper supplyAddressMapper;
	
	//供货联系人mapper
	@Autowired
	private ContactMapper contactMapper;
	
	//分页查询数据
	public List<Supplier> getSupplierByPage(Integer page, Integer size) {
		// TODO Auto-generated method stub
		
		Integer start = 0;
		if(page >= 0 ) {//判断页数
			start = (page - 1) * size;
		}
		List<Supplier> suppliers = supplierMapper.getSupplierByPage(start, size, LoginInterceptor.getLoginUser().getCompanyId());
		
		for(Supplier supplier : suppliers) {
			if(supplier.getCounty() != null) {
				List<String> area = new ArrayList<String>();
				
				area.add(supplier.getCountryId());
				area.add(supplier.getProvinceId());
				area.add(supplier.getCityId());
				area.add(supplier.getCountyId());
				
				
				
				supplier.setArea(area);
				supplier.setBeat(supplier.getCountry().getName()+"/"+supplier.getProvince().getName()+"/"+supplier.getCity().getName()+"/"+supplier.getCounty().getName());
					
				
			}
			
			for(SupplyAddress supplyAddress : supplier.getSupplyAdresses()) {
				List<String> areas = new ArrayList<String>();
				
				areas.add(supplyAddress.getCountryId());
				areas.add(supplyAddress.getProvinceId());
				areas.add(supplyAddress.getCityId());
				areas.add(supplyAddress.getCountyId());
				
				supplyAddress.setBeat(supplyAddress.getCountry().getName() + "/" + supplyAddress.getProvince().getName() + "/" + supplyAddress.getCity().getName() + "/" + supplyAddress.getCounty().getName());
			}
		}
		return suppliers;
	}

	//获取总数
	public long getCount() {
		// TODO Auto-generated method stub
		Long count = supplierMapper.getCount();
		return count;
	}

	//新增数据
	public void add(Supplier supplier) {
		// TODO Auto-generated method stub
		//id
		String supplierId = UUID.randomUUID().toString();
		//所属公司id
		String companyId = LoginInterceptor.getLoginUser().getCompanyId();
		//创建人id
		String systemUserId = LoginInterceptor.getLoginUser().getId();
		
		supplier.setId(supplierId);
		supplier.setCompanyId(companyId);
		supplier.setSystemUserId(systemUserId);
		supplier.setCreateTime(new Date());
		
		//设置区域各级id
		if(supplier.getArea().size() > 0) {
			supplier.setCountryId(supplier.getArea().get(0));
			supplier.setProvinceId(supplier.getArea().get(1));
			supplier.setCityId(supplier.getArea().get(2));
			supplier.setCountyId(supplier.getArea().get(3));
		}
		
		supplierMapper.add(supplier);
		
		List<Contact> contactList = supplier.getContacts();
		
		if(contactList.size() > 0) {
			for(Contact contact : contactList) {
				contact.setId(UUID.randomUUID().toString());
				contact.setSupplierId(supplierId);
				contactMapper.add(contact);
			}
		}
		
		List<SupplyAddress> supplyAddresses = supplier.getSupplyAdresses();
		if(supplyAddresses.size() > 0) {
			for(SupplyAddress supplyAddress : supplyAddresses) {
				supplyAddress.setId(UUID.randomUUID().toString());
				supplyAddress.setSupplierId(supplierId);
				supplyAddressMapper.add(supplyAddress);
			}
		}
		
	}

	//修改数据
	public void update(Supplier supplier) {
		// TODO Auto-generated method stub
		if(supplier != null) {
			
			//更新主表数据
			supplierMapper.update(supplier);
			
			//取出供货地联系人
			List<Contact> contacts = supplier.getContacts();
			
			if(contacts.size() > 0) {//判断联系人集合是否>0，循环修改数据
				for(Contact contact : contacts) {
					contactMapper.update(contact);
				}
			}
			
			//取出供货地址
			List<SupplyAddress> supplyAddresses = supplier.getSupplyAdresses();
			
			if(supplyAddresses.size() > 0) {
				for(SupplyAddress supplyAddress : supplyAddresses) {
					supplyAddressMapper.update(supplyAddress);
				}
			}
			
		}
		
	}

}
