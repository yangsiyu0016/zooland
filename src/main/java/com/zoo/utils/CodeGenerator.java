package com.zoo.utils;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.UUID;

import com.zoo.filter.LoginInterceptor;
import com.zoo.model.erp.GeneratorCode;
import com.zoo.service.erp.GeneratorCodeService;

public class CodeGenerator {
	private volatile static CodeGenerator codeGenerator;
	
	private GeneratorCodeService generatorCodeService;
	private CodeGenerator() {};
	
	public static CodeGenerator getInstance() {
		if(codeGenerator==null) {
			synchronized (CodeGenerator.class) {
				if(codeGenerator==null) {
					codeGenerator = new CodeGenerator();
				}
			}
		}
		return codeGenerator;
	}
	
	public String generator(String parameter) {
		generatorCodeService = (GeneratorCodeService)ApplicationUtil.getBean("generatorCodeService");
		String code="";
		if(parameter.indexOf("<")==-1) {
			String prefix = parameter.substring(0, parameter.indexOf("*"));
			int length = parameter.substring(parameter.indexOf("*")).length();
			code+=prefix;
			
			// 得到一个NumberFormat的实例  
	        NumberFormat nf = NumberFormat.getInstance(); 
	        // 设置是否使用分组  
	        nf.setGroupingUsed(false);  
	        // 设置最大整数位数  
	        nf.setMaximumIntegerDigits(length);  
	        // 设置最小整数位数  
	        nf.setMinimumIntegerDigits(length);
	        
	        GeneratorCode generatorCode = generatorCodeService.getGCode(prefix,length,null,null);
	        if(generatorCode!=null) {
	        	code+=nf.format(generatorCode.getNumber()+1);
	        	generatorCodeService.updateNumber(generatorCode.getId(),generatorCode.getNumber()+1);
	        }else {
	        	code+=nf.format(1);
	        	generatorCode = new GeneratorCode();
	        	generatorCode.setId(UUID.randomUUID().toString());
	        	generatorCode.setPrefix(prefix);
	        	generatorCode.setLength(length);
	        	generatorCode.setNumber(1);
	        	generatorCode.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
	        	generatorCodeService.addGeneratorCode(generatorCode);
	        }
		}else {
			String prefix = parameter.substring(0, parameter.indexOf("<"));
			int length = parameter.substring(parameter.indexOf("*")).length();
			
			code+=prefix;
			Calendar now = Calendar.getInstance();
			String dateString = parameter.substring(parameter.indexOf("<"), parameter.lastIndexOf(">")+1);
			String dateValue="";
			if(dateString.contains("YEAR")) {
				dateValue+=now.get(Calendar.YEAR);
			}
			if(dateString.contains("MONTH")) {
				int month = now.get(Calendar.MONTH)+1;
				if(month<10) {
					dateValue+=("0"+month);
				}else {
					dateValue+=month;
				}
			}
			if(dateString.contains("DAY")) {
				int day = now.get(Calendar.DAY_OF_MONTH);
				if(day<0) {
					dateValue+=("0"+day);
				}else {
					dateValue+=day;
				}
			}
			code+=dateValue;
			
			// 得到一个NumberFormat的实例  
	        NumberFormat nf = NumberFormat.getInstance(); 
	        // 设置是否使用分组  
	        nf.setGroupingUsed(false);  
	        // 设置最大整数位数  
	        nf.setMaximumIntegerDigits(length);  
	        // 设置最小整数位数  
	        nf.setMinimumIntegerDigits(length);
	        
	        GeneratorCode generatorCode = generatorCodeService.getGCode(prefix,length,dateString,dateValue);
	        if(generatorCode!=null) {
	        	code+=nf.format(generatorCode.getNumber()+1);
	        	generatorCodeService.updateNumber(generatorCode.getId(),generatorCode.getNumber()+1);
	        }else {
	        	code+=nf.format(1);
	        	generatorCode = new GeneratorCode();
	        	generatorCode.setId(UUID.randomUUID().toString());
	        	generatorCode.setPrefix(prefix);
	        	generatorCode.setLength(length);
	        	generatorCode.setNumber(1);
	        	generatorCode.setDateStr(dateString);
	        	generatorCode.setDateValue(dateValue);
	        	generatorCode.setCompanyId(LoginInterceptor.getLoginUser().getCompanyId());
	        	generatorCodeService.addGeneratorCode(generatorCode);
	        }
		}
		
		return code;
	}
	public static void main(String[] args) {
		String parameter = "QC<YEAR><MONTH><DAY>****";
		//String prefix = parameter.substring(0, parameter.indexOf("<"));
		//int length = parameter.substring(parameter.indexOf("*")).length();
		String dateString = parameter.substring(parameter.indexOf("<"), parameter.lastIndexOf(">")+1);
		//System.out.println(parameter.substring(parameter.indexOf("<"), parameter.lastIndexOf(">")+1));
		System.out.println(dateString.contains("YEAR"));
		Calendar now = Calendar.getInstance();
		System.out.println(now.get(Calendar.YEAR));
		System.out.println(now.get(Calendar.MONTH)+1);
		System.out.println(now.get(Calendar.DAY_OF_MONTH));
		String qc = "QC";
		qc+=("0"+(now.get(Calendar.MONTH)+1));
		System.out.println(qc);
		String aa = "http://192.168.1.237:8081/productimage/6b835123-7af3-406d-b80e-eb706d059d53.jpg";
		System.out.println(aa.substring(aa.lastIndexOf("/")));
	}
}
