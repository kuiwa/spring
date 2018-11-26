package com.mymaven.java8.lambda;

import java.util.Optional;

import org.testng.annotations.Test;

public class OptionalTest {

    @Test
    public void runTest() {
        Optional<String> optionalStr = Optional.of("Hello");
        System.out.println("OptionalStr value : " + optionalStr);

        String testStr = null;
        System.out.println("testStr default value : " + Optional.ofNullable(testStr).orElse("Else value"));

        Optional<String> optionalTestStr = Optional.ofNullable(testStr);
        System.out.println("testStr default value : " + optionalTestStr);
        System.out.println("testStr default value : " + optionalTestStr.orElse("Second else value"));
        System.out.println("testStr default value : "
                + optionalTestStr.orElseThrow(() -> new IllegalArgumentException("null argum")));
    }
}
