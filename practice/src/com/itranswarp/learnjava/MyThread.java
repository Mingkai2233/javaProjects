package com.itranswarp.learnjava;

public class MyThread extends Thread {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("main start");
        MyThread mt = new MyThread();
        mt.start();
        mt.join();
        System.out.println("main end");

    }
    @Override
    public void run() {
        System.out.printf("MyThread[%d] is running\n", Thread.currentThread().threadId());
    }
}
