package com.mymaven.java8.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.testng.annotations.Test;

public class LambdaTestA {

    //    @Test
    public void mapTest() {
        List<Double> cost = Arrays.asList(10.0, 20.0, 30.0);
        cost.stream().map(x -> x + x * 0.05).forEach(x -> System.out.println(x));
    }

    //    @Test
    public void functionTest() {
        System.out.println("Start testing");
        applyFunctionAndPrint("Good", (s) -> s + s + 2);
        applyFunctionAndPrint("Good", (s) -> s.toLowerCase());
        applyConsumerAndPrint("Afternoon", System.out::println);

        Function<String, String> f = (s) -> s.toUpperCase();
        applyFunctionAndPrint("My", f);
        applyFunctionAndPrint("Dear", f);
        System.out.println("End testing");
    }

    //    @Test
    public void forEach() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        list.forEach(System.out::println);
    }

    @Test
    public void streamTestA() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        list.stream().filter(i -> i < 5).forEach(System.out::println);
    }

    private <R> R applyFunctionAndPrint(R object, Function<R, R> fun) {
        System.out.println("Before: " + object);
        R res = fun.apply(object);
        System.out.println("After: " + res);
        return res;
    }

    private <R> void applyConsumerAndPrint(R object, Consumer<R> fun) {
        System.out.println("Before: " + object);
        fun.accept(object);
    }
}
