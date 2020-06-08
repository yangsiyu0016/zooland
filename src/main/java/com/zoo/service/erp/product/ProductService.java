package com.zoo.service.erp.product;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.zoo.config.UploadProperties;
import com.zoo.controller.erp.constant.GeneratorCodeType;
import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.GeneratorCodeMapper;
import com.zoo.mapper.erp.product.ProductDetailMapper;
import com.zoo.mapper.erp.product.ProductMapper;
import com.zoo.mapper.erp.product.ProductSkuMapper;
import com.zoo.mapper.erp.product.ProductTypeMapper;
import com.zoo.model.erp.GeneratorCode;
import com.zoo.model.erp.product.Product;
import com.zoo.model.erp.product.ProductDetail;
import com.zoo.model.erp.product.ProductSku;
import com.zoo.model.erp.product.ProductType;

@Service
@Transactional
public class ProductService {
	@Autowired
	ProductMapper productMapper;
	@Autowired
	ProductSkuMapper skuMapper;
	@Autowired
	ProductDetailMapper productDetailMapper;
	@Autowired
    private UploadProperties prop;
	@Autowired
	ProductTypeMapper typeMapper;
	@Autowired
    private FastFileStorageClient storageClient;
	@Autowired
	GeneratorCodeMapper generatorCodeMapper;
	public String uploadImage(MultipartFile file) {
		//保存图片
        try {
            String extensionName = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extensionName, null);
            //返回保存图片的完整url
            return prop.getBaseUrl() + storePath.getFullPath();
        } catch (IOException e) {
        	e.printStackTrace();
            throw new ZooException(ExceptionEnum.UPLOAD_IMAGE_EXCEPTION);
        }
	}
	public List<Product> getProductByPage(Integer page,Integer size){
		Integer start = null;
		if(page!=null&&size!=null) {
			start = (page-1)*size;
		}
		
		List<Product> products = productMapper.getProductByPage(start, size, LoginInterceptor.getLoginUser().getCompanyId());
		handleType(products);
		return products;
	}
	private void handleType(List<Product> products) {
		for(Product product:products) {
			String typeId = product.getTypeId();
			ProductType type = typeMapper.getTypeById(typeId);
			String name = buildName(type.getName(),type);
			product.setTypeName(name);
		}
		
	}
	private String buildName(String name,ProductType type) {
		if(!StringUtils.isBlank(type.getParentId())) {
			ProductType parentType = typeMapper.getTypeById(type.getParentId());
			name=parentType.getName()+"/"+name;
			buildName(name,parentType);
		}
		return name;
	}
	public Long getCount() {
		return productMapper.getCount(LoginInterceptor.getLoginUser().getCompanyId());
	}

	
	public void addProduct(Product product) {
		product.setId(UUID.randomUUID().toString());
		product.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		product.setCtime(new Date());
		
		// 得到一个NumberFormat的实例  
        NumberFormat nf = NumberFormat.getInstance(); 
        String code = "";
        Map<String,Object> condition = new HashMap<String,Object>();
        String typeId = product.getTypeId();
        List<String> typeCodes = new ArrayList<String>();
        typeCodes = buildTypeCodes(typeCodes,typeId);
		
        Collections.reverse(typeCodes);
        String initCode = "";
        for(String typeCode:typeCodes) {
        	initCode+=typeCode;
        }
        
        condition.put("initCode", initCode);
		condition.put("type", GeneratorCodeType.PRODUCT);
		condition.put("companyId", LoginInterceptor.getLoginUser().getCompanyId());
		GeneratorCode generatorCode = generatorCodeMapper.getGeneratorCodeByCondition(condition);
		
		if(generatorCode!=null){
			int number =generatorCode.getNumber();
			// 设置是否使用分组  
	        nf.setGroupingUsed(false);  
	        // 设置最大整数位数  
	        nf.setMaximumIntegerDigits(4);  
	        // 设置最小整数位数  
	        nf.setMinimumIntegerDigits(4); 
	        code = (initCode+=nf.format(number+1));
	        condition = new HashMap<String,Object>();
	        condition.put("id", generatorCode.getId());
	        condition.put("number", number+1);
	        generatorCodeMapper.updateNumber(condition);
		}else{
			generatorCode = new GeneratorCode();
			generatorCode.setId(UUID.randomUUID().toString());
			generatorCode.setType(GeneratorCodeType.PRODUCT);
			generatorCode.setNumber(1);
			generatorCode.setInitCode(initCode);
			generatorCode.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
			generatorCodeMapper.insertGeneratorCode(generatorCode);
			
			code = (initCode+="0001");
		}
		product.setCode(code);
		productMapper.addProduct(product);
		
		ProductDetail productDetail = product.getProductDetail();
		productDetail.setProductId(product.getId());
		productDetailMapper.addProductDetail(productDetail);
		
		saveSku(product);
		
	}
	private List<String> buildTypeCodes(List<String> typeCodes,String parentId) {
		
		ProductType type = typeMapper.getTypeById(parentId);
		typeCodes.add(type.getCode());
		if(StringUtils.isNotBlank(type.getParentId())) {
			buildTypeCodes(typeCodes,type.getParentId());
		}
		return typeCodes;
	}
	public List<ProductSku> getSkuByProductId(String productId){
		return skuMapper.getSkuByProductId(productId);
	} 
	private void saveSku(Product product) {
		List<ProductSku> skuList = product.getSkus();
		if(skuList.size()>0) {
			for(ProductSku sku:skuList) {
				sku.setId(UUID.randomUUID().toString());
				sku.setProductId(product.getId());
				
				/**生成编码开始**/
				// 得到一个NumberFormat的实例  
		        NumberFormat nf = NumberFormat.getInstance(); 
		        String code = "";
		        String initCode = product.getCode();
		        Map<String,Object> condition = new HashMap<String,Object>();
		        

		        
		        condition.put("initCode", initCode);
				condition.put("type", GeneratorCodeType.SKU);
				condition.put("companyId", LoginInterceptor.getLoginUser().getCompanyId());
				GeneratorCode generatorCode = generatorCodeMapper.getGeneratorCodeByCondition(condition);
				
				if(generatorCode!=null){
					int number =generatorCode.getNumber();
					// 设置是否使用分组  
			        nf.setGroupingUsed(false);  
			        // 设置最大整数位数  
			        nf.setMaximumIntegerDigits(4);  
			        // 设置最小整数位数  
			        nf.setMinimumIntegerDigits(4); 
			        code = (initCode+=nf.format(number+1));
			        condition = new HashMap<String,Object>();
			        condition.put("id", generatorCode.getId());
			        condition.put("number", number+1);
			        generatorCodeMapper.updateNumber(condition);
				}else{
					generatorCode = new GeneratorCode();
					generatorCode.setId(UUID.randomUUID().toString());
					generatorCode.setType(GeneratorCodeType.SKU);
					generatorCode.setNumber(1);
					generatorCode.setInitCode(initCode);
					generatorCode.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
					generatorCodeMapper.insertGeneratorCode(generatorCode);
					
					code = (initCode+="0001");
				}
				sku.setCode(code);
				/**生成编码结束**/
				skuMapper.addSku(sku);
			}
		}else {
			ProductSku sku = new ProductSku();
			sku.setId(UUID.randomUUID().toString());
			sku.setProductId(product.getId());
			
			/**生成编码开始**/
			// 得到一个NumberFormat的实例  
	        NumberFormat nf = NumberFormat.getInstance(); 
	        String code = "";
	        String initCode = product.getCode();
	        Map<String,Object> condition = new HashMap<String,Object>();
	        

	        
	        condition.put("initCode", initCode);
			condition.put("type", GeneratorCodeType.SKU);
			condition.put("companyId", LoginInterceptor.getLoginUser().getCompanyId());
			GeneratorCode generatorCode = generatorCodeMapper.getGeneratorCodeByCondition(condition);
			
			if(generatorCode!=null){
				int number =generatorCode.getNumber();
				// 设置是否使用分组  
		        nf.setGroupingUsed(false);  
		        // 设置最大整数位数  
		        nf.setMaximumIntegerDigits(4);  
		        // 设置最小整数位数  
		        nf.setMinimumIntegerDigits(4); 
		        code = (initCode+=nf.format(number+1));
		        condition = new HashMap<String,Object>();
		        condition.put("id", generatorCode.getId());
		        condition.put("number", number+1);
		        generatorCodeMapper.updateNumber(condition);
			}else{
				generatorCode = new GeneratorCode();
				generatorCode.setId(UUID.randomUUID().toString());
				generatorCode.setType(GeneratorCodeType.SKU);
				generatorCode.setNumber(1);
				generatorCode.setInitCode(initCode);
				generatorCode.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
				generatorCodeMapper.insertGeneratorCode(generatorCode);
				
				code = (initCode+="0001");
			}
			sku.setCode(code);
			/**生成编码结束**/
			skuMapper.addSku(sku);
		}
		
	}

}
