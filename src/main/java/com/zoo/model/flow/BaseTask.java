package com.zoo.model.flow;

import java.util.Date;

import lombok.Data;

@Data
public class BaseTask {
	private String id;
	private String formKey;
	private String name;
	private String originatorName;//发起人名称
	private String assignee;//待办人ID
	private String assigneeName;
	private Date createTime;//创建时间
	private long stateTime; //停留时间
	private String businessKey;//业务主键
}
