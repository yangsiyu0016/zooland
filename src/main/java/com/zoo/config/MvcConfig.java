package com.zoo.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zoo.filter.LoginInterceptor;
import com.zoo.properties.JwtProperties;
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class MvcConfig implements WebMvcConfigurer {
	@Autowired
    private JwtProperties props;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //配置登录拦截器
        registry.addInterceptor(new LoginInterceptor(props)).addPathPatterns("/**");
    }

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/productimage/**").addResourceLocations("file:/home/mjnbzoo/static/productimage/");
		WebMvcConfigurer.super.addResourceHandlers(registry);
	}

	
    
}
