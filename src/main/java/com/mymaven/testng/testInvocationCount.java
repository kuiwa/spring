package com.mymaven.testng;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class TestInvocationCount {

//    @BeforeSuite
//    @Parameters({"suitePara"})
//    public void beforeSuite(@Optional("suitePara") String suitePara) {
//        System.out.println("beforeSuite");
//    }
    @Test(invocationCount = 1)
    @Parameters({ "para1" })
    public void testMethod1(@Optional("default") String para1) {
        System.out.println("testMethod1");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test(invocationCount = 1)
    public void testMethod2() {
        System.out.println("testMethod2");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test(invocationCount = 1)
    public void testMethod3() {
        System.out.println("testMethod3");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
