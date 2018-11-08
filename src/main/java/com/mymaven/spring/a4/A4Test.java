package com.mymaven.spring.a4;

import com.mymaven.spring.TestAppTemplate;

public class A4Test extends TestAppTemplate {

    // Spring 注入集合
    @Override
    public void test() {
        context = super.constructXml("bean-a4.xml");
        JavaCollection jc = (JavaCollection) context.getBean("javaCollection");
        jc.getAddressList();
        jc.getAddressSet();
        jc.getAddressMap();
        jc.getAddressProp();
    }

}
