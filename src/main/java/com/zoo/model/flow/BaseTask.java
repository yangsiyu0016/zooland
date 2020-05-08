package com.zoo.model.flow;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class BaseTask {
	private String id;
	private String formKey;
	private String name;
	private String taskKey;
	private String originatorName;//发起人名称
	private String assignee;//待办人ID
	private String assigneeName;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;//创建时间
	private long stateTime; //停留时间
	private String businessKey;//业务主键
}
