package com.zoo.model.annex;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class Annex {
	
	private String id;//主键id
	private String title;//标题
	private String fileName;//文件名称
	private String url;//文件路径
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date utime;//上传时间
	private String foreignKey;//业务id
	
	private String suffix;//文件格式
	private String size;//文件大小
	
}
