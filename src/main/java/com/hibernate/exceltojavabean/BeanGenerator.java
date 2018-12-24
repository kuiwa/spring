package com.hibernate.exceltojavabean;

public class BeanGenerator {

    private String beanString = "";

    protected void formatBeanSwitchBoxConfigurationClass(String path) {
        addNewLine();
        addTabIncent(4);
        formatReference(path);

    }

    protected void addNewLine() {
        setBeanString(getBeanString() + "\n");
    }

    protected void addTabIncent(int incentTimes) {
        for (int i = 1; i <= incentTimes; i++)
            setBeanString(getBeanString() + "    ");
    }

    protected void formatReference(String path) {
        setBeanString(getBeanString() + String.format("<ref bean=\"%s\" />", path));
    }
    
    protected void addStringSwitchPathClass(String path) {
        setBeanString(getBeanString() + String
                .format("    <bean id=\"%s\" class=\"com.ericsson.radio.test.ctr.helpers.arptoinstrumentpath.configuration.SwitchPath\">",
                        path));
        addNewLine();
    }

    protected void setPropertyForSwitches() {
        setBeanString(getBeanString() + "<property name=\"switches\">");        
    }

    public void addString(String string) {
        setBeanString(getBeanString() + string);
    }
    
    protected void generateSwitchChildReference(String switchParent, String referenceId, int position) {
        addTabIncent(1);
        String parentId = "parent" + switchParent;
        setBeanString(getBeanString() + String.format("<bean id=\"%s\" parent=\"%s\">", referenceId, parentId));
        addNewLine();
        addTabIncent(2);
        setBeanString(getBeanString() + String.format("<property name=\"position\" value = \"%d\" />", position));
        addNewLine();
        addTabIncent(1);
        setBeanString(getBeanString() + String.format("</bean>"));
        addNewLine();
        addNewLine();
    }
    
    protected void addListEnd(){
        setBeanString(getBeanString() + "</list>");
    }
    
    protected void addPropertyEnd(){
        setBeanString(getBeanString() + "</property>");
    }
    
    protected void addBeanEnd(){
        setBeanString(getBeanString() + "</bean>");
    }

    protected void addSwitchClass(String path, int i) {
        beanString += String
                .format("    <bean id=\"parent%s\" class=\"com.ericsson.radio.test.ctr.helpers.arptoinstrumentpath.configuration.Switch\">",
                        path);
        addNewLine();
        beanString += String.format("        <property name=\"name\" value=\"%s\" />", path);
        addNewLine();
        beanString += String.format("        <property name=\"order\" value=\"%s\" />", i);
        addNewLine();
        beanString += String.format("    </bean>");
        addNewLine();
        addNewLine();
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
        this.beanString = beanString;
    }
    
    
}
