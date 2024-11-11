package com.itranswarp.learnjava;

import java.io.*;

public class CopyFile {
    public static void main(String[] args) {
        if (args.length%2 != 0){
            System.out.println("参数有误，请重试");
            return;
        }
        for (int i = 0; i < args.length; i+=2) {
            copyFile(args[i], args[i + 1]);
        }
    }

    static void copyFile(String sourceFile, String targetFile) {
        try(InputStream input = new FileInputStream(sourceFile);
            OutputStream output = new FileOutputStream(targetFile)) {
            input.transferTo(output);
            System.out.println(sourceFile+"->"+targetFile+" success!");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
