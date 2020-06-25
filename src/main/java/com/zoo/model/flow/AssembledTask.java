package com.zoo.model.flow;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AssembledTask extends BaseTask{
	private String code;
}
