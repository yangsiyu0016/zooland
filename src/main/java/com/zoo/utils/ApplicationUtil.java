package com.zoo.utils;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 手动获取Spring上下文和Bean对象
 *
 * @Author YinWenBing
 * @Date 2017/1/6  17:07
 */
@Component("applicationUtil")
public class ApplicationUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    /**
     * 加载Spring配置文件时，如果Spring配置文件中所定义的Bean类实现了ApplicationContextAware接口，会自动调用该方法
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    	context = applicationContext;
    }

    /**
     * 获取Spring上下文
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return context;
    }

    /**
     * 获取Spring Bean
     * @param name
     * @return
     * @throws BeansException
     */
    public static Object getBean(String name) throws BeansException {
        return context.getBean(name);
    }
}