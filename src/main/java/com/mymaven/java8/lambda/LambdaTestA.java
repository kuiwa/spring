package com.mymaven.java8.lambda;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

public class LambdaTestA {

    @Test
    public void mapTest() {
        List<Double> cost = Arrays.asList(10.0, 20.0, 30.0);
        cost.stream().map(x -> x + x * 0.05).forEach(x -> System.out.println(x));
    }
}
