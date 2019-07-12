package com.mymaven.testng;

import org.testng.ISuite;
import org.testng.ISuiteListener;


public class EnvListener implements ISuiteListener{

    private static final int TIMER = 3;
    private int time = 0;
    private long t1 = System.currentTimeMillis();
    
    @Override
    public void onStart(ISuite suite) {
        System.out.println("Start time : " + t1);
    }

    @Override
    public void onFinish(ISuite suite) {
//        System.out.println("Finish time : " + time);
//        System.out.println("suite parameter : " + suite.getParameter("suitePara"));
//        System.out.println("method parameter : " + suite.getParameter("para1"));
//        if (time++ < TIMER) {
//        suite.run();
//        }
        
        long t2 = System.currentTimeMillis();    
        System.out.println("Duration : " + (t2 - t1) / 1000);
        if (t2 - t1 < Integer.parseInt(suite.getParameter("sustaintime")) * 1 * 1000) {
        suite.run();
        }
    }

}
