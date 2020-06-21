package com.zoo.service.erp.assembled;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.controller.erp.constant.AssembledStatus;
import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.assembled.ProductAssembledMapper;
import com.zoo.mapper.erp.assembled.ProductAssembledMaterialMapper;
import com.zoo.model.erp.assembled.ProductAssembled;
import com.zoo.model.erp.assembled.ProductAssembledMaterial;
import com.zoo.service.system.parameter.SystemParameterService;
import com.zoo.utils.CodeGenerator;

@Service
@Transactional
public class ProductAssembledService {
	@Autowired
	ProductAssembledMapper paMapper;
	@Autowired
	SystemParameterService systemParameterService;
	@Autowired
	ProductAssembledMaterialMapper pamMapper;
	public List<ProductAssembled> getProductAssembledByPage(Integer page, Integer size) {
		Integer start = (page-1)*size;
		List<ProductAssembled> productAssembleds = paMapper.getProductAssembledByPage(start,size,LoginInterceptor.getLoginUser().getCompanyId());
		return productAssembleds;
	}
	public long getCount() {
		// TODO Auto-generated method stub
		return paMapper.getCount(LoginInterceptor.getLoginUser().getCompanyId());
	}
	public ProductAssembled getProductAssembledById(String id) {
		return paMapper.getProductAssembledById(id);
	}
	public void addProductAssembled(ProductAssembled productAssembled) {
		String id = UUID.randomUUID().toString();
		productAssembled.setId(id);
		if(productAssembled.getCodeGeneratorType().equals("AUTO")) {
			try {
				String parameterValue = systemParameterService.getValueByCode("C00006");
				String code = CodeGenerator.getInstance().generator(parameterValue);
				productAssembled.setCode(code);
			} catch (Exception e) {
				throw new ZooException(ExceptionEnum.GENER_CODE_ERROR);
			}
		}
		productAssembled.setCuserId(LoginInterceptor.getLoginUser().getId());
		productAssembled.setCtime(new Date());
		productAssembled.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		productAssembled.setStatus(AssembledStatus.WTJ);
		paMapper.addProductAssembled(productAssembled);
		
		for(ProductAssembledMaterial material:productAssembled.getMaterials()) {
			material.setId(UUID.randomUUID().toString());
			material.setProductAssembledId(id);
			pamMapper.addMaterial(material);
		}
	}
	public void updateProductAssembled(ProductAssembled productAssembled) {

		paMapper.updateProductAssembled(productAssembled);
		pamMapper.deleteMaterialByAllembledId(productAssembled.getId());
		for(ProductAssembledMaterial material:productAssembled.getMaterials()) {
			material.setId(UUID.randomUUID().toString());
			material.setProductAssembledId(productAssembled.getId());
			pamMapper.addMaterial(material);
		}
	}
	public void deleteProductAssembledById(String ids) {
		String[] split = ids.split(",");
		for(String productAssembledId:split) {
			//删除材料
			pamMapper.deleteMaterialByAllembledId(productAssembledId);

		}
		paMapper.deleteProductAssembledById(split);
		
	}
	
}
