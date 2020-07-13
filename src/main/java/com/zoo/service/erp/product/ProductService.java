package com.zoo.service.erp.product;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.util.StringUtil;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.zoo.config.UploadProperties;
import com.zoo.enums.ExceptionEnum;
import com.zoo.exception.ZooException;
import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.GeneratorCodeMapper;
import com.zoo.mapper.erp.product.ProductMapper;
import com.zoo.mapper.erp.product.ProductTypeMapper;
import com.zoo.model.erp.product.Product;
import com.zoo.model.erp.product.ProductType;
import com.zoo.service.system.parameter.SystemParameterService;
import com.zoo.utils.CodeGenerator;

import net.sf.json.JSONObject;

@Service
@Transactional
@PropertySource("classpath:downloadAddress.properties")
public class ProductService {
	@Autowired
	ProductMapper productMapper;
	@Autowired
    private UploadProperties prop;
	@Autowired
	ProductTypeMapper typeMapper;
	@Autowired
    private FastFileStorageClient storageClient;
	@Autowired
	GeneratorCodeMapper generatorCodeMapper;
	@Autowired
	SystemParameterService systemParameterService;
	
	@Value("${sourceIp}")
	private String sourceIp;
	
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
	public List<Product> getProductByPage(Integer page,Integer size, String sort,String order,String keywords, String typeId,String brandId,String name,String code,String mnemonic){
		Integer start = null;
		if(page!=null&&size!=null) {
			start = (page-1)*size;
		}
		
		List<Product> products = productMapper.getProductByPage(
				start, size,sort,order, keywords, typeId,brandId,name,code,mnemonic, LoginInterceptor.getLoginUser().getCompanyId());
		handleType(products);
		return products;
	}
	private void handleType(List<Product> products) {
		for(Product product:products) {
			String typeId = product.getTypeId();
			if(StringUtil.isNotEmpty(typeId)) {
				ProductType type = typeMapper.getTypeById(typeId);
				String name = buildName(type.getName(),type);
				product.setTypeName(name);
			}
			
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
	public Long getCount(String keywords, String typeId,String brandId,String name,String code,String mnemonic) {
		return productMapper.getCount( keywords, typeId,brandId,name,code,mnemonic,LoginInterceptor.getLoginUser().getCompanyId());
	}

	
	public void addProduct(String productString,MultipartFile file) throws Exception {
		
		JSONObject jsonObject = JSONObject.fromObject(productString);
		Product product = (Product) JSONObject.toBean(jsonObject, Product.class);
		product.setId(UUID.randomUUID().toString());
		product.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
		product.setCtime(new Date());
		
		String parameterValue = systemParameterService.getValueByCode("c00001");
		String code = CodeGenerator.getInstance().generator(parameterValue);
		product.setCode(code);
		
		if(file!=null) {
			String fileName = file.getOriginalFilename();
			//获取文件的后缀名
			String suffix = fileName.substring(fileName.lastIndexOf("."));
			
			fileName = UUID.randomUUID().toString()+suffix;
			//拼接下载url
			//String url = sourceIp;
			
			String projectPath = System.getProperty("user.dir");//获取当前项目路径
			//拼接上传路径
			String uploadUrl = projectPath + "/static/productimage/" + fileName;
			
			//判断该文件是否存在，
			File uploadFile = new File(uploadUrl);
			if(file != null) {
				file.transferTo(uploadFile);
			}
			
			product.setImageUrl(sourceIp+"/productimage/" + fileName);
			
		}
		
		productMapper.addProduct(product);
		
	}
	public void updateProduct(String productString, MultipartFile file) throws Exception {
		
		JSONObject jsonObject = JSONObject.fromObject(productString);
		Product product = (Product) JSONObject.toBean(jsonObject, Product.class);
		
		if(file!=null) {
			Product old = this.getProductById(product.getId());
			
			
			String fileName = file.getOriginalFilename();
			//获取文件的后缀名
			String suffix = fileName.substring(fileName.lastIndexOf("."));
			
			fileName = UUID.randomUUID().toString()+suffix;
			//拼接下载url
			//String url = sourceIp;
			
			String projectPath = System.getProperty("user.dir");//获取当前项目路径
			
			String imageUrl = old.getImageUrl();
			if(StringUtil.isNotEmpty(imageUrl)) {
				String deleteUrl = projectPath+"/static/productimage/"+imageUrl.substring(imageUrl.lastIndexOf("/"));
				new File(deleteUrl).delete();
			}
			//拼接上传路径
			String uploadUrl = projectPath + "/static/productimage/" + fileName;
			
			//p判断该产品是否删除之前图片
			/*
			 * Product product2 = productMapper.getProductById(product.getId()); String url
			 * = product2.getImageUrl(); String[] split = null; if(url != null &&
			 * !"".equals(url)) { split = url.split("/"); } url = projectPath +
			 * "/static/productimage/" + split[split.length -1]; boolean flag = new
			 * File(url).delete();
			 */
			//if(flag) {
				//判断该文件是否存在，
				File uploadFile = new File(uploadUrl);
				if(file != null) {
					file.transferTo(uploadFile);
				}
				
				product.setImageUrl(sourceIp+"/productimage/" + fileName);
			//}
			
			
		}
		
		productMapper.updateProduct(product);
	}
	public Product getProductById(String id) {
		Product product = productMapper.getProductById(id);
		return product;
	}
	
	/**
	 * 删除
	 * @param ids
	 */
	public int deleteProductById(String ids) {
		// TODO Auto-generated method stub
		String[] split = ids.split(",");
		int num = productMapper.deleteProductById(split);
		
		return num;
	}
	
	/**
	 * 根据typeId获取产品
	 */
	public List<Product> getProductByTypeId(Integer page, Integer size, String typeId) {
		Integer start = null;
		if(page != null) {
			start = (page - 1) * size;
		}
		return productMapper.getProductByTypeId(start, size, LoginInterceptor.getLoginUser().getCompanyId(), typeId);
	}
	public Long getCountByTypeId(String typeId) {
		return productMapper.getCountByTypeId(LoginInterceptor.getLoginUser().getCompanyId(), typeId);
	}
	
	public void updateHasBom(String id,Boolean hasBom) {
		productMapper.updateHasBom(id, hasBom);
	}
}
