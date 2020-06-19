package com.zoo.service.erp.product;

import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.product.MaterialStructureMapper;
import com.zoo.model.erp.product.MaterialStructure;
import com.zoo.model.erp.product.MaterialStructureDetail;
import com.zoo.model.system.user.UserInfo;

@Service
@Transactional
public class MaterialStructureService {
	@Autowired
	MaterialStructureMapper msMapper;
	@Autowired
	MaterialStructureDetailService msdService;
	@Autowired
	ProductService productService;
	
	public List<MaterialStructure> getMaterialStructureByPage(Integer page,Integer size){
		UserInfo uinfo = LoginInterceptor.getLoginUser();
		int start = (page-1)*size;
		List<MaterialStructure> mss = msMapper.getMaterialStructureByPage(start, size, uinfo.getCompanyId());
		return mss;
	}
	public Long getCount() {
		UserInfo uinfo = LoginInterceptor.getLoginUser();
		return msMapper.getCount(uinfo.getCompanyId());
	}
	public MaterialStructure getMaterialStructureByProductId(String productId) {
		return msMapper.getMaterialStructureByProductId(productId);
	}
	public MaterialStructure addMaterialStructure(MaterialStructure ms) {
		
		if(ms.getProduct().getHasBom()) {
			throw new ZooException("已建立了该产品的物资材料清单，不能再新增");
		}else {
			ms.setId(UUID.randomUUID().toString());
			ms.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
			msMapper.addMaterialStructure(ms);
			
			productService.updateHasBom(ms.getProduct().getId(), true);
			
			for(MaterialStructureDetail detail:ms.getDetails()) {
				if(detail.getProduct().getId().equals(ms.getProduct().getId())) {
					throw new ZooException("子件不能包含父件");
				}
				detail.setMaterialStructureId(ms.getId());
				msdService.addMaterialStructureDetail(detail);
			}
		}
		
		return ms;
	}
	public MaterialStructure getMaterialStructureById(String id) {
		// TODO Auto-generated method stub
		return msMapper.getMaterialStructureById(id);
	}
	public MaterialStructure updateMaterialStructure(MaterialStructure ms) {
		msMapper.updateMaterialStructure(ms);
		return ms;
	}
	public void deleteMaterialStructureById(String ids) {
		String[] split = ids.split(",");
		for(String id:split) {
			
			msdService.deleteDetailByMaterialStructureId(id);
			
			MaterialStructure ms = this.getMaterialStructureById(id);
			productService.updateHasBom(ms.getProduct().getId(), false);
		}
		msMapper.deleteMaterialStructureById(split);
		
	}
}
