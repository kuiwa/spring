package com.mymaven.spring.a5;

import java.util.logging.Logger;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mymaven.spring.ConfigurationData;
import com.mymaven.spring.TestAppTemplate;

public class A5Test extends TestAppTemplate {

    static Logger log = Logger.getLogger(A5Test.class.getName());

    @Override
    public void test() {
        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("spring-bean/bean-a5.xml");
        context.start();
        ConfigurationData config = (ConfigurationData) context.getBean("withListener");
        config.getMessage();
        context.stop();
        log.info("Exiting the program.");
    }

}
