package com.itranswarp.learnjava;




public class PrintABCD {
    static volatile Integer ID = 0;
    static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException{
        int threadNumber = 4;
        Thread[] threads = new Thread[threadNumber];
        for (int i = 0; i < threadNumber; i++) {
            threads[i] = new Thread(new Task(i, threadNumber));
            threads[i].start();
        }
        for (int i = 0; i < threadNumber; i++) {
            threads[i].join();
        }
    }


}
class Task implements Runnable {
    private int id;
    private int threadNumber;
    public Task(int id, int threadNumber) {
        this.id = id;
        this.threadNumber = threadNumber;
    }

    @Override
    public void run() {
        while (true){
            synchronized (PrintABCD.lock){
                while (this.id != PrintABCD.ID){
                    try{
                        PrintABCD.lock.wait();
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                System.out.printf("%c", 'A'+this.id);
                PrintABCD.ID = (PrintABCD.ID+1)%this.threadNumber;
                PrintABCD.lock.notifyAll();
            }
        }

    }
}
