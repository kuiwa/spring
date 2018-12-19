package com.mymaven.spring.a1;

import com.mymaven.spring.ConfigurationData;
import com.mymaven.spring.TestAppTemplate;

public class A1Test extends TestAppTemplate {

    @Override
    public void test() {
        context = super.constructXml("bean-a1.xml");
        ConfigurationData config = (ConfigurationData) context.getBean("helloWorldChild");
        config.getMessage();
    }

}
