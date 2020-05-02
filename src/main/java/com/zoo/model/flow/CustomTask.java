package com.zoo.model.flow;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class CustomTask {
	private String code;
	
	private String id;
	private String name;
	private String assignee;
	private String assigneeName;
	private String formKey;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date createTime;
	private String executionId;
	private String processInstanceId;//流程实例ID
	private String processDefinitionId;//流程定义ID
	private Date dueDate;//到期日期
	private int priority; //优先级
	//流程发起人
	private String originatorName;
	//流程名称
	private String workflowName;
	private String key;
	private long stateTime;//任务停留时间 分钟
	
	private String customerName;
	private String supplierName;
}
