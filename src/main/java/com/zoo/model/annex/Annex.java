package com.zoo.model.annex;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class Annex {
	
	private String id;//主键id
	private String title;//文件名称
	private String url;//文件路径
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date utime;//上传时间
	private String foreignKey;//业务id
	
}
