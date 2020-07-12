package com.zoo.service.annex;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.util.StringUtil;
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
		public Annex addAnnex(Annex annex) {
			annex.setId(UUID.randomUUID().toString());
			//annex.setUtime(new Date());
			annexMapper.addAnnex(annex);
			return annex;
		}
		public void delAnnexFile(Annex annex) {
			if(StringUtil.isNotEmpty(annex.getId())) {
				annexMapper.delAnnexById(annex.getId());
			}
			String projectPath = System.getProperty("user.dir");//获取当前项目路径
			
			//拼接上传路径
			String uploadUrl = projectPath + "/static/annexFile/" + annex.getFileName();
			new File(uploadUrl).delete();
			
		}
		public int delAnnexByForeignKey(String foreignKey) {
			return annexMapper.delAnnexByForeignKey(foreignKey);
			
		}
	
}
