package org.onjava8;// streams/RandomWords.java
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;
import java.nio.file.*;
public class RandomWords implements Supplier<String> {
    List<String> words = new ArrayList<>();
    Random rand = new Random(11);
    RandomWords(String fname) throws IOException, URISyntaxException {
        URL url = RandomWords.class.getResource(fname);
        Path path = Paths.get(url.toURI());
        //Path path = Paths.get(fname);
        List<String> lines = Files.readAllLines(path);
        // 略过第一行
        for (String line : lines.subList(1, lines.size())) {
            for (String word : line.split("[ .?,]+"))
                words.add(word.toLowerCase());
        }
    }
    @Override
    public String get() {
        return words.get(rand.nextInt(words.size()));
    }
    @Override
    public String toString() {
        return words.stream()
            .collect(Collectors.joining(" "));
    }
    public static void main(String[] args) throws Exception {
        System.out.println(System.getProperty("user.dir"));
        System.out.println(
            Stream.generate(new RandomWords("Cheese.dat"))
                .limit(10)
                .collect(Collectors.joining(" ")));
    }
}