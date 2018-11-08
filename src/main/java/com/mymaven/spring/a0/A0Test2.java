package com.mymaven.spring.a0;

import com.mymaven.spring.ConfigurationData;
import com.mymaven.spring.TestAppTemplate;

public class A0Test2 extends TestAppTemplate{

    // scope="singleton"
    public void test() {
        context = super.constructXml("bean-a0.xml");
        ConfigurationData config = (ConfigurationData) context.getBean("singleton");
        config.getMessage();

        config.setMessage("Hello Chengdu.");
        config.getMessage();
        
        ConfigurationData configB = (ConfigurationData) context.getBean("singleton");
        configB.getMessage();
    }

}
