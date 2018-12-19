package com.mymaven.spring;

public class ConfigurationData {

    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    //    @ReturnValue(default = "Hello world.")
    public void getMessage() {
        System.out.println("Your Message : " + message);
    }

    public void init() {
        System.out.println("Bean is going through init.");
    }

    public void destroy() {
        System.out.println("Bean will destroy now.");
    }
}
