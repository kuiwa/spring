package com.mymaven.spring.a0;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mymaven.spring.ConfigurationData;
import com.mymaven.spring.TestAppTemplate;

public class A0Test3 extends TestAppTemplate {

    // init-method="init" destroy-method="destroy" in bean
    public void test() {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("spring-bean/bean-a0.xml");
        ConfigurationData config = (ConfigurationData) context.getBean("helloWorld");
        config.getMessage();

        config.setMessage("Hello Chengdu.");
        config.getMessage();
    }
}
