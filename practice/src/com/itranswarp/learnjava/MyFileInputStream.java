package com.itranswarp.learnjava;

import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MyFileInputStream extends FilterInputStream {
    public static void main(String[] args) throws IOException {
        try(MyFileInputStream input = new MyFileInputStream(new FileInputStream(".\\src\\test.tmp"))){
            byte[] buf = new byte[1024];
            int n = input.read(buf, 0, buf.length);
            System.out.println(n);
            System.out.println("read data size:"+input.getByteCount());
        }
        System.out.println(System.getProperty("java.class.path"));
    }
    private int count = 0;
    MyFileInputStream(InputStream in) throws IOException {
        super(in);
    }

    int getByteCount(){
        return this.count;
    }

    @Override
    public int read() throws IOException {
        int n = super.read();
        if (n != -1){
            count++;
        }
        return n;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int n = super.read(b, off, len);
        if (n != -1){
            count += n;
        }
        return n;
    }
}
