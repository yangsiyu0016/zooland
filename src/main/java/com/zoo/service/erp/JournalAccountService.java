package com.zoo.service.erp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoo.filter.LoginInterceptor;
import com.zoo.mapper.erp.JournalAccountMapper;
import com.zoo.mapper.erp.product.SpecParamMapper;
import com.zoo.model.erp.JournalAccount;
import com.zoo.model.erp.product.ProductSku;
import com.zoo.model.erp.product.SpecParam;

import net.sf.json.JSONObject;

@Service
@Transactional
public class JournalAccountService {
	@Autowired
	JournalAccountMapper jaMapper;
	@Autowired
	SpecParamMapper paramMapper;
	public List<JournalAccount> getJournalAccountByPage(Integer page,Integer size){
		Integer start = (page-1)*size;
		List<JournalAccount> list = jaMapper.getJournalAccountByPage(start, size, LoginInterceptor.getLoginUser().getCompanyId());
		buildSpec(list);
		return list;
	}
	private void buildSpec(List<JournalAccount> list) {
		List<String> built = new ArrayList<String>();
		for(JournalAccount ja:list) {
			ProductSku sku = ja.getStock().getProductSku();
			if(!built.contains(sku.getProduct().getId())){
				//通用规格参数处理
				String genericSpec = sku.getProduct().getProductDetail().getGenericSpec();
				Map<String,String> map = new HashMap<String,String>();
				JSONObject obj = JSONObject.fromObject(genericSpec);
				Set<String> keyset = obj.keySet();
				for(String key:keyset) {
					SpecParam param = paramMapper.getParamById(key);
					map.put(param.getName(), StringUtils.isBlank(obj.getString(key))?"其它":obj.getString(key));
				}
				sku.getProduct().getProductDetail().setGenericSpec(map.toString());
				
				
				String ownSpec = sku.getOwnSpec();
				 map = new HashMap<String,String>();
				 obj  = JSONObject.fromObject(ownSpec);
				keyset = obj.keySet();
				for(String key:keyset) {
					SpecParam param = paramMapper.getParamById(key);
					map.put(param.getName(), obj.getString(key));
				}
				sku.setOwnSpec(map.toString());
				ja.getStock().setProductSku(sku);
				
				built.add(sku.getProduct().getId());
			}
			
		}
		
	}
	public long getCount() {
		return jaMapper.getCount(LoginInterceptor.getLoginUser().getCompanyId());
	}
	public int addJournalAccount(JournalAccount journalAccount) {
		return jaMapper.addJournalAccount(journalAccount);		
	}

}
