package com.itranswarp.learnjava;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InputStreamPrac {
    public static void main(String[] args) throws IOException {
        Random rand = new Random(3);
        byte[] nums = new byte[10];
        for (int i = 0; i < 10; i++) {
            nums[i] = (byte)rand.nextInt('A', 'Z');
        }

        try(ByteArrayInputStream input = new ByteArrayInputStream(nums)){
            System.out.println(readAsString(input));
        }
    }
    static String readAsString(InputStream input) throws IOException {
        int n;
        StringBuilder sb = new StringBuilder();
        while ((n = input.read())!=-1){
            sb.append((char)n);
        }
        return sb.toString();
    }
}
