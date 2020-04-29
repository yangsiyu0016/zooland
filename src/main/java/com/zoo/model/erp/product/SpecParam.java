package com.zoo.model.erp.product;

import lombok.Data;

@Data
public class SpecParam {
	private String id;
	private String typeId;
	private String groupId;
	private String name;
	private boolean isNumeric;//是否为数值，boolean类型，true则为数值，false则不是。为空也代表非数值
	private boolean generic;//是否为通用属性
	private boolean searching;//是否用于搜索
	private String segments;//搜索区间
	private String unit;
}
