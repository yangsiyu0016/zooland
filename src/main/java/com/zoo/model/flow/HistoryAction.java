package com.zoo.model.flow;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class HistoryAction {
	private String actName;
	private String actType;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date stime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date etime;
	private String message;
	private String assigneeName;
	private String assignee;
	private int duration;
}
