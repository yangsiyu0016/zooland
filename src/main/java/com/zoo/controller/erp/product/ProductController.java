package com.zoo.controller.erp.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zoo.model.erp.product.Product;
import com.zoo.service.erp.product.ProductService;
import com.zoo.vo.RespBean;

@RestController
@RequestMapping("/product")
public class ProductController {
	@Autowired
	ProductService productService;
	/**
     * 图片的上传
     *
     * @param file
     * @return
     */
    @PostMapping("uploadImage")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(productService.uploadImage(file));
    }
    @GetMapping("page")
    public Map<String,Object> getProductByPage(
    		@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size){
    	Map<String,Object> map = new HashMap<String,Object>();
    	List<Product> products = productService.getProductByPage(page, size);
    	long count = productService.getCount();
    	map.put("products", products);
    	map.put("count", count);
    	return map;
    }
    @GetMapping("getProductById")
    public Product getProductById(@RequestParam("id")String id) {
    	return productService.getProductById(id);
    }
    @PostMapping("add")
    public RespBean addProduct(@RequestParam("product")String product,@RequestParam(value="file",required = false) MultipartFile file) {
    	try {
    		//System.out.println(request.getParameter("product"));
    		productService.addProduct(product,file);
			return new RespBean("200","添加成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
    }
    @PostMapping("update")
    public RespBean updateProduct(@RequestBody Product product) {
    	try {
    		productService.updateProduct(product);
			return new RespBean("200","更新成功");
		} catch (Exception e) {
			return new RespBean("500",e.getMessage());
		}
    }
}
