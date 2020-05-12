package com.zoo.service.annex;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoo.mapper.annex.AnnexMapper;
import com.zoo.model.annex.Annex;

@Service
@Transactional
public class AnnexService {

	@Autowired
	private AnnexMapper annexMapper;
	
	//根据id获取上传文件数据
		public Annex getAnnexById(String id) {
			Annex annex = annexMapper.getAnnexById(id);
			return annex;
		}
		//根据业务id获取上传文件数据
		public List<Annex> getAnnexsByForeignKey(String id){
			return annexMapper.getAnnexsByForeignKey(id);
		}
		//根据id删除上传文件数据
		public int delAnnexById(String id) {
			return annexMapper.delAnnexById(id);
		}
		//添加上传文件信息
		public void addAnnex(Annex annex) {
			annex.setId(UUID.randomUUID().toString());
			annex.setUtime(new Date());
			annexMapper.addAnnex(annex);
		}
	
}
