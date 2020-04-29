package com.zoo.service.erp.warehouse;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.warehouse.WarehouseMapper;
import com.zoo.model.erp.warehouse.GoodsAllocation;
import com.zoo.model.erp.warehouse.Warehouse;
import com.zoo.model.system.user.SystemUser;
import com.zoo.model.system.user.UserInfo;

@Service
@Transactional
public class WarehouseService {
	@Autowired
	WarehouseMapper warehouseMapper;
	@Autowired
	GoodsAllocationService gaService;
	public List<Warehouse> getWarehouseByPage(Integer page,Integer size){
		Integer start = null;
		if(page!=null&&size!=null) {
			start = (page-1)*size;
		} 
		UserInfo uinfo = LoginInterceptor.getLoginUser();
		
		return warehouseMapper.getWarehouseByPage(start, size, uinfo.getCompanyId());
	}
	public Long getCount() {
		UserInfo uinfo = LoginInterceptor.getLoginUser();
		return warehouseMapper.getCount(uinfo.getCompanyId());
	}
	public void addWarehouse(Warehouse warehouse) {
		UserInfo uinfo = LoginInterceptor.getLoginUser();
		String warehouseId = UUID.randomUUID().toString();
		warehouse.setId(warehouseId);
		warehouse.setCtime(new Date());
		warehouse.setStatus(true);
		warehouse.setCompanyId(uinfo.getCompanyId());
		warehouseMapper.addWarehouse(warehouse);
		
		for(SystemUser manager:warehouse.getManagers()) {
			warehouseMapper.addManager(UUID.randomUUID().toString(), warehouseId, manager.getId());
		}

		for(GoodsAllocation ga:warehouse.getGoodsAllocations()) {
			ga.setId(UUID.randomUUID().toString());
			ga.setWarehouseId(warehouseId);
			gaService.addGoodsAllocation(ga);
		}
	}
	public void updateWarehouse(Warehouse warehouse) {
		warehouseMapper.updateWarehouse(warehouse);
		warehouseMapper.deleteWUByWarehouseId(warehouse.getId());
		for(SystemUser manager:warehouse.getManagers()) {
			warehouseMapper.addManager(UUID.randomUUID().toString(), warehouse.getId(), manager.getId());
		}
	}
	public Warehouse getWarehouseById(String id) {
		// TODO Auto-generated method stub
		return warehouseMapper.getWarehouseById(id);
	}

}
