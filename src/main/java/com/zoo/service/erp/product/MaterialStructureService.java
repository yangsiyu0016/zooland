package com.zoo.service.erp.product;

import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.exception.ZooException;
import com.zoo.mapper.erp.product.MaterialStructureMapper;
import com.zoo.model.erp.product.MaterialStructure;
import com.zoo.model.erp.product.MaterialStructureDetail;

@Service
@Transactional
public class MaterialStructureService {
	@Autowired
	MaterialStructureMapper msMapper;
	@Autowired
	MaterialStructureDetailService msdService;
	@Autowired
	ProductService productService;
	public void addMaterialStructure(MaterialStructure ms) {
		ms.setId(UUID.randomUUID().toString());
		msMapper.addMaterialStructure(ms);
		
		productService.updateHasBom(ms.getProduct().getId(), true);
		
		for(MaterialStructureDetail detail:ms.getDetails()) {
			if(detail.getProduct().getId().equals(ms.getProduct().getId())) {
				throw new ZooException("子件不能包含父件");
			}
			detail.setId(UUID.randomUUID().toString());
			detail.setMeterialStuctureId(ms.getId());
			msdService.addMaterialStructureDetail(detail);
		}
	}
}
