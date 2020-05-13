package com.zoo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Date2StringUtils {

	public static String Date2String(Date date) {
		if(date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String format = sdf.format(date);
			return format;
		}else {
			return null;
		}
		
	}
	public static String Object2String(Object object) {
		if (object != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String format = sdf.format(object);
			return format;
		}else {
			return null;
		}
	}
}
