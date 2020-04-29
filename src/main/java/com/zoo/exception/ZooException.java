package com.zoo.exception;


import com.zoo.enums.ExceptionEnum;

import lombok.Getter;

/**
 * @author bystander
 * @date 2018/9/15
 *
 * 自定义异常类
 */
@Getter
public class ZooException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ExceptionEnum exceptionEnum;

    public ZooException(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }


}
