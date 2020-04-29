package com.zoo.model.system.menu;

import java.util.List;
import lombok.Data;

@Data
public class SystemMenu {
	private String id;
	private String url;
	private String path;
	private String component;
	private String name;
	private String title;
	private String iconCls;
	private String parentId;
	private String companyId;
	private String type;
	private boolean enabled;
	private List<SystemMenu> children;
	private MenuMeta meta;
}
