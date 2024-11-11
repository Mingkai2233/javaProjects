package com.itranswarp.learnjava;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamPrac {
    public static void main(String[] args) {
        try(OutputStream out = new FileOutputStream("src\\test.tmp")){
            out.write("hello lei jun!".getBytes());

            System.out.println("写入成功！");
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

    }
}
