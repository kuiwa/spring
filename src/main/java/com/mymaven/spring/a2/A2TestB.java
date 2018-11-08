package com.mymaven.spring.a2;

import com.mymaven.spring.TestAppTemplate;

public class A2TestB extends TestAppTemplate {

    // Spring 注入内部 Beans
    // https://www.w3cschool.cn/wkspring/t7n41mm7.html
    @Override
    public void test() {
        context = super.constructXml("bean-a2-TextEditorB.xml");
        TextEditorB te = (TextEditorB) context.getBean("textEditor");
        te.spellCheck();
    }

}
