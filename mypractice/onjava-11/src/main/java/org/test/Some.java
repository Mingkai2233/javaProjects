package org.test;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.stream.IntStream.range;

public class Some {
    public static void main(String[] args) {
//        new Random(11).ints().distinct().forEach(System.out::println);
        System.out.println(range(1, 10).sum());
    }
}
