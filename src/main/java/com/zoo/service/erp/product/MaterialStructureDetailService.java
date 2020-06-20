package com.zoo.service.erp.product;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.exception.ZooException;
import com.zoo.mapper.erp.product.MaterialStructureDetailMapper;
import com.zoo.model.erp.product.MaterialStructure;
import com.zoo.model.erp.product.MaterialStructureDetail;

@Service
@Transactional
public class MaterialStructureDetailService {
	@Autowired
	MaterialStructureDetailMapper msdMapper;
	@Autowired
	MaterialStructureService msService;
	public MaterialStructureDetail addMaterialStructureDetail(MaterialStructureDetail msd) {
		String materialStructureId = msd.getMaterialStructureId();
		MaterialStructure materialStructure =msService.getMaterialStructureById(materialStructureId);
		
		for(MaterialStructureDetail detail:materialStructure.getDetails()) {
			if(detail.getProduct().getId().equals(msd.getProduct().getId())) {
				throw new ZooException("子件不能重复");
			}
		}
		if(msd.getProduct().getId().equals(materialStructure.getProduct().getId())) {
			throw new ZooException("子件不能与父件相同");
		}
		boolean canSave = check(msd,materialStructure.getProduct().getId());
		if(!canSave) {
			throw new ZooException("存在子父构件循环");
		}
		msd.setId(UUID.randomUUID().toString());
		msdMapper.addMaterialStructureDetail(msd);
		return msd;
	}
	
	private Boolean check(MaterialStructureDetail msd,String productId) {
		Boolean flag = true;
		if(msd.getProduct().getHasBom()) {//添加的产品已有bom
			MaterialStructure materialStructure = msService.getMaterialStructureByProductId(msd.getProduct().getId());
			for(MaterialStructureDetail detail:materialStructure.getDetails()) {
				if(detail.getProduct().getId().equals(productId)) {
					flag = false;
					break;
				}else {
					check(detail,productId);
				}
			}
		}
		return flag;
	}

	public MaterialStructureDetail updateMaterialStructureDetail(MaterialStructureDetail detail) {
		msdMapper.updateMaterialStructureDetail(detail);
		return detail;
	}
	public void deleteDetailById(String ids) {
		String[] split = ids.split(",");
		msdMapper.deleteDetailById(split);
		
	}

	public void deleteDetailByMaterialStructureId(String materialStructureId) {
		msdMapper.deleteDetailByMaterialStructureId(materialStructureId);
		
	}
}
