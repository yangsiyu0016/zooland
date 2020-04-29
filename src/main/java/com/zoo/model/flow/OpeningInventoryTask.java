package com.zoo.model.flow;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class OpeningInventoryTask extends BaseTask {
	private String code;
}
