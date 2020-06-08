package com.zoo.mapper.annex;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zoo.model.annex.Annex;

@Component
public interface AnnexMapper {

	//根据id获取上传文件数据
	public Annex getAnnexById(@Param("id") String id);
	//根据业务id获取上传文件数据
	public List<Annex> getAnnexsByForeignKey(@Param("id") String id);
	//根据id删除上传文件数据
	public int delAnnexById(@Param("id") String id);
	//添加上传文件信息
	public void addAnnex(@Param("annex") Annex annex);
	
	//根据主键id修改annex表得业务id
	public void updateAnnexForeignKeyById(@Param("annex") Annex annex);
	public int delAnnexByForeignKey(@Param("foreignKey")String foreignKey);
}
