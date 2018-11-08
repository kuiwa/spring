package com.mymaven.spring.a2;

import com.mymaven.spring.TestAppTemplate;

public class A2Test extends TestAppTemplate {

    // https://www.w3cschool.cn/wkspring/t7n41mm7.html
    @Override
    public void test() {
        context = super.constructXml("bean-a2-TextEditor.xml");
        TextEditor te = (TextEditor) context.getBean("textEditor");
        te.spellCheck();
    }

}
