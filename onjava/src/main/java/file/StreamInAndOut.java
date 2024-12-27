package file;// files/StreamInAndOut.java
import java.io.*;
import java.nio.file.*;
import java.util.stream.*;

public class StreamInAndOut {
    public static void main(String[] args) throws IOException {
        //Files.createFile(Paths.get("StreamInAndOut.txt"));
        Path path = Paths.get("D:\\javaProjects\\onjava\\src\\main\\java\\file\\StreamInAndOut.java");
        System.out.println(path);
        try(
          Stream<String> input =
            Files.lines(path);
          PrintWriter output =
            new PrintWriter("StreamInAndOut.txt")
        ) {
            input.map(String::toUpperCase)
              .forEachOrdered(output::println);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}