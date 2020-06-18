package com.zoo.service.erp.product;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.mapper.erp.product.MaterialStructureDetailMapper;
import com.zoo.model.erp.product.MaterialStructureDetail;

@Service
@Transactional
public class MaterialStructureDetailService {
	@Autowired
	MaterialStructureDetailMapper msdMapper;
	public void addMaterialStructureDetail(MaterialStructureDetail msd) {
		msdMapper.addMaterialStructureDetail(msd);
	}
}
