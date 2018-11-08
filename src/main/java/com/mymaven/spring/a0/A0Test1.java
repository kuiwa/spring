package com.mymaven.spring.a0;

import org.junit.Test;

import com.mymaven.spring.ConfigurationData;
import com.mymaven.spring.TestAppTemplate;

public class A0Test1 extends TestAppTemplate {

    public void test() {
        context = super.constructXml("bean-a0.xml");
        ConfigurationData config = (ConfigurationData) context.getBean("helloWorld");
        config.getMessage();

        config.setMessage("Hello Chengdu.");
        config.getMessage();
    }

}
