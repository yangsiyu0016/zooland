package com.zoo.utils;

import java.text.NumberFormat;
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
	        
	        GeneratorCode generatorCode = generatorCodeService.getGCode(prefix,length);
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
			
		}
		
		return code;
	}
	public static void main(String[] args) {
		String parameter = "SP*****";
		System.out.println(parameter.indexOf("<"));
		System.out.println(parameter.substring(0, parameter.indexOf("*")));
		System.out.println(parameter.substring(parameter.indexOf("*")).length());
		// 得到一个NumberFormat的实例  
        NumberFormat nf = NumberFormat.getInstance(); 
        // 设置是否使用分组  
        nf.setGroupingUsed(false);  
        // 设置最大整数位数  
        nf.setMaximumIntegerDigits(5);  
        // 设置最小整数位数  
        nf.setMinimumIntegerDigits(5);
        System.out.println("SP"+nf.format(999999));
	}
}
