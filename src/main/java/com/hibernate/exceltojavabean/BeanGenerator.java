package com.hibernate.exceltojavabean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class BeanGenerator {

    private String beanString = "";
    private String beanId;
    private String className;
    private List<String> refs = new ArrayList<>();
    private Map<String, String> properties = new HashMap<>();
    private String propertyNameForList;

    /**
     * @return the refsOfSwitchPath
     */
    public List<String> getRefsOfSwitchPath() {
        return refs;
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

    /**
     * @return the beanId
     */
    public String getBeanId() {
        return beanId;
    }

    /**
     * @param beanId the beanId to set
     */
    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }

    /**
     * @param className the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return the properties
     */
    public Map<String, String> getProperties() {
        return properties;
    }

    /**
     * @return the refs
     */
    public List<String> getRefs() {
        return refs;
    }

    protected String buildHead() {
        ResourceBundle modelPropertiesBundle = ResourceBundle.getBundle("model");
        //        if (modelPropertiesBundle.containsKey(("beanHead"))) {
        return modelPropertiesBundle.getString("beanHead");
        //        }
    }

    protected String buildEnd() {
        return "</beans>";
    }

    protected void setPropertyNameForList(String name) {
        propertyNameForList = name;
    }

    protected void build() {
        setBeanString(String.format("\t<bean id=\"%s\" class=\"%s\">\n", beanId, className));
        properties.forEach((k, v) -> setBeanString(String.format("\t\t<property name=\"%s\" value=\"%s\" />\n", k, v)));
        if (!refs.isEmpty()) {
            setBeanString(String.format("\t\t<property name=\"%s\">\n", propertyNameForList));
            setBeanString("\t\t\t<list>\n");
            refs.forEach((ref) -> setBeanString(String.format("\t\t\t\t<ref bean=\"%s\" />\n", ref)));
            setBeanString("\t\t\t</list>\n");
            setBeanString("\t\t</property>\n");
        }
        setBeanString("\t</bean>\n\n");
    }

    protected void buildParent(String beanId, String parent) {
        setBeanString(String.format("\t<bean id=\"%s\" parent=\"%s\">\n", beanId, parent));
        properties.forEach((k, v) -> setBeanString(String.format("\t\t<property name=\"%s\" value=\"%s\" />\n", k, v)));
        setBeanString("\t</bean>\n\n");
    }

}
