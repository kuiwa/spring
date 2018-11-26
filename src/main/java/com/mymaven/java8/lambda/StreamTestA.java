package com.mymaven.java8.lambda;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

public class StreamTestA {

    @Test
    public void runTest() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 0);
        list.parallelStream().filter(i -> i > 1).sequential().forEach(System.out::println);
        list.stream().filter(i -> i > 1).map(i -> "String " + i).forEach(System.out::println);
    }
}
