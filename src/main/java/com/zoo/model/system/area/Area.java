package com.zoo.model.system.area;

import java.util.List;

import lombok.Data;

@Data
public class Area {
	private String id;
	private String name;
	private String type;
	private Area parent;
	private String parentId;
	private List<Area> children;
	private Boolean leaf;
	
}
