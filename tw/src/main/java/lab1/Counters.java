package lab1;

public class Counters {

    static private final int iters = 100000000;

    public static class Counter {
        int count = 0;
        public void printCounter(){
            System.out.println(count);
        }

        public void increment(){count++;}

        public void decrement(){count--;}
    }


    public static class SyncCounter {
        int count = 0;

        void printCounter() {
            System.out.println(count);
        }

        synchronized void increment() {
            count++;
        }

        synchronized void decrement() {count--;}
    }

    static Counter counter = new Counter();
    static SyncCounter syncCounter = new SyncCounter();

    public static class UnsyncDecrementer extends Thread {
        synchronized public void run(){
            for(int i = 0; i < iters; i++) counter.decrement();
        }
    }

    public static class UnsyncIncrementer extends Thread {

        synchronized public void run(){
            for(int i = 0; i < iters; i++) counter.increment();
        }
    }

    public static class SyncDecrementer extends Thread {
        synchronized public void run(){
            for(int i = 0; i < iters; i++) syncCounter.decrement();
        }
    }

    public static class SyncIncrementer extends Thread {

        synchronized public void run(){
            for(int i = 0; i < iters; i++) syncCounter.increment();
        }
    }


    public static void main(String[] args) throws InterruptedException {

        Thread unsyncThread1 = new UnsyncIncrementer();
        Thread unsyncThread2 = new UnsyncDecrementer();

        unsyncThread1.start();
        unsyncThread2.start();

        unsyncThread1.join();
        unsyncThread2.join();

        System.out.print("Not synchronized result: ");
        counter.printCounter();

        Thread syncThread1 = new SyncIncrementer();
        Thread syncThread2 = new SyncDecrementer();

        syncThread1.start();
        syncThread2.start();

        syncThread1.join();
        syncThread2.join();

        System.out.print("Synchronized result: ");
        syncCounter.printCounter();
    }

}
