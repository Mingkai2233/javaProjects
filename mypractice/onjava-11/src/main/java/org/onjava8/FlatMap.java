package org.onjava8;// streams/FlatMap.java
import java.util.Iterator;
import java.util.List;
import java.util.stream.*;
public class FlatMap {
    public static void main(String[] args) {
        List<String> collect = Stream.of(1, 2, 3)
                .flatMap(i -> Stream.of("Gonzo", "Fozzie", "Beaker"))
                .collect(Collectors.toList());
        for (String s : collect) {
            System.out.println(s);
        }

    }
}