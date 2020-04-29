package com.zoo;


import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class,
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
@MapperScan("com.zoo.mapper")
public class ZoolandApplication 
{
    public static void main( String[] args )
    {
        SpringApplication.run(ZoolandApplication.class, args);
    }
}
