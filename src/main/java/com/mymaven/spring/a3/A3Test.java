package com.mymaven.spring.a3;

import com.mymaven.spring.TestAppTemplate;


public class A3Test extends TestAppTemplate{

    // https://www.w3cschool.cn/wkspring/t7n41mm7.html
    @Override
    public void test() {
        context = super.constructXml("bean-a3-Foo.xml");
        Foo foo = (Foo) context.getBean("foo");
    }

}
