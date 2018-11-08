package com.mymaven.spring;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class TestAppTemplate {

    protected ApplicationContext context;

    public ApplicationContext constructXml(String xmlFileName) {
        return constructXml("spring-bean/", xmlFileName);
    }

    public ApplicationContext constructXml(String xmlPath, String xmlFileName) {
        ApplicationContext context = new ClassPathXmlApplicationContext(xmlPath + xmlFileName);
        return context;
    }

    public Object getBean(ApplicationContext context, String property) {
        return context.getBean(property);
    }

    //    public void getMessage(ConfigurationData config) {
    //        config.getMessage();
    //    }

    @Test
    public abstract void test();
}
