package com.zoo.utils;

import org.joda.time.DateTime;

public class OrderCodeHelper{
	private static final Object Locker = new Object();
	
	private static  String _tempId = "";
	/**
	 * 生成订单号
	 * @param primacy
	 * @return
	 * @throws Exception
	 */
	public static String GenerateId(String primacy) throws Exception{
		synchronized(Locker){
			String orderId = primacy+DateTime.now().toString("yyyyMMddHHmmss");
			if(orderId.equals(_tempId)){
				throw new Exception("订单号重复");
			}
			_tempId = orderId;
			Thread.sleep(1000);
			return orderId;
		}
	}

	
	
	
}
