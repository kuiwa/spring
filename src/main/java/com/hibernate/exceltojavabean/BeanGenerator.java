package com.hibernate.exceltojavabean;

import java.util.ArrayList;
import java.util.List;


public class BeanGenerator {

    private String beanString = "";
    protected String beanId;
    protected String className;
    protected List<String> properties = new ArrayList<>();
    protected List<String> refs = new ArrayList<>();

    protected void addTabIncent(int incentTimes) {
        for (int i = 1; i <= incentTimes; i++)
            setBeanString("    ");
    }

    protected void addString(String string) {
        setBeanString(string);
    }

    /**
     * @return the beanString
     */
    public String getBeanString() {
        return beanString;
    }

    /**
     * @param beanString the beanString to set
     */
    public void setBeanString(String beanString) {
        this.beanString += beanString;
    }

    protected void setBeanClassBegin(String beanId, String clazz) {
        setBeanString(String.format("<bean id=\"%s\" class=\"%s\">\n", beanId, clazz));
    }

    protected void setBeanChildBegin(String beanId, String parentId) {
        setBeanString(String.format("<bean id=\"%s\" parent=\"%s\">\n", beanId, parentId));
    }

    protected void setBeanEnd() {
        setBeanString("</bean>\n\n");
    }

    protected void setPropertyBegin(String name) {
        setBeanString(String.format("<property name=\"%s\">\n", name));
    }

    protected void setProperty(String name, String value) {
        setBeanString(String.format("<property name=\"%s\" value=\"%s\" />\n", name, value));
    }

    protected void setPropertyEnd() {
        setBeanString("</property>\n");
    }

    protected void setListBegin() {
        setBeanString("<list>\n");
    }

    protected void setListEnd() {
        setBeanString("</list>\n");
    }

    protected void setRefCall(String refId) {
        setBeanString(String.format("<ref bean=\"%s\" />\n", refId));
    }

    protected void addComment(String comment) {
        setBeanString(String.format("<!-- %s -->\n", comment));
    }

    protected void addBean(BeanGenerator beanGeneratorSwitchBoxConfigurationPropertySwitchPaths) {
        setBeanString(beanGeneratorSwitchBoxConfigurationPropertySwitchPaths.getBeanString());
    }

}
