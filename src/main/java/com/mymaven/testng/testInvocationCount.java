package com.mymaven.testng;

import org.testng.annotations.Test;


public class testInvocationCount {

    @Test(invocationCount = 10)
    public void testMethod1() {
        System.out.println("testMethod1");
    }
    
    @Test(invocationCount = 10)
    public void testMethod2() {
        System.out.println("testMethod2");
    }
    
    @Test(invocationCount = 10)
    public void testMethod3() {
        System.out.println("testMethod3");
    }
}
