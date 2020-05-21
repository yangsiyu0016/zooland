package com.zoo.model.flow;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class InventoryCheckTask extends BaseTask {
	private String code;
}
