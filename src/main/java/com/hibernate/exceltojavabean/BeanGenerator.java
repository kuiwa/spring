package com.hibernate.exceltojavabean;

public class BeanGenerator {

    private String beanString = "";

    protected void addNewLine() {
        setBeanString(getBeanString() + "\n");
    }

    protected void addTabIncent(int incentTimes) {
        for (int i = 1; i <= incentTimes; i++)
            setBeanString(getBeanString() + "    ");
    }

    protected void addString(String string) {
        setBeanString(getBeanString() + string);
    }

    protected void addListEnd() {
        setBeanString(getBeanString() + "</list>");
    }

    protected void addPropertyEnd() {
        setBeanString(getBeanString() + "</property>");
    }

    protected void addBeanEnd() {
        setBeanString(getBeanString() + "</bean>");
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
